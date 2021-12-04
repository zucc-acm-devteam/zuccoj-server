package top.kealine.zuccoj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.entity.UserEdit;
import top.kealine.zuccoj.entity.UserFull;
import top.kealine.zuccoj.entity.UserRank;
import top.kealine.zuccoj.service.CaptchaService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;
import top.kealine.zuccoj.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CaptchaService captchaService;

    @Autowired
    UserController(UserService userService, CaptchaService captchaService) {
        this.userService = userService;
        this.captchaService = captchaService;
    }

    @RequestMapping(value = "/check", method = {RequestMethod.POST, RequestMethod.GET})
    public Map<String, Object> check(
            @RequestParam(name = "level", required = true) int level,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), level)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.succeedMessage();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password,
            HttpServletRequest request) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseConstant.X_USER_NOT_FOUND;
        }
        if (!userService.checkUser(user, password)) {
            return ResponseConstant.X_USER_WRONG_PASSWORD;
        }
        if (!userService.checkUserPermission(user, PermissionLevel.COMMON)) {
            return ResponseConstant.X_USER_FORBIDDEN;
        }
        userService.saveUserToSession(request.getSession(), user);
        userService.userAccess(username, IpUtil.getIpAddr(request));
        return ResponseConstant.V_USER_LOGIN_SUCCESS;
    }

    @RequestMapping(value = "/ssoLogin", method = RequestMethod.POST)
    public Map<String, Object> ssoLogin(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "ticket", required = true) String ticket,
            HttpServletRequest request
    ) {
        String checkUrl = "https://api.zuccacm.top/sso/v1/ticket/check";
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(checkUrl);
        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("username", username);
        jsonNode.put("ticket", ticket);
        try {
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.setEntity(new StringEntity(jsonNode.toString(), StandardCharsets.UTF_8));
            HttpResponse httpResponse = client.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                return ResponseConstant.X_SSO_CHECK_FAILED;
            }
        } catch (IOException e) {
            return ResponseConstant.X_SSO_CHECK_FAILED;
        }
        if (!userService.hasUser(username)){
            String password = RandomStringUtils.randomAlphanumeric(20);
            userService.newUser(username, username, password, "", "");
        }
        User user = userService.getUserByUsername(username);
        if (!userService.checkUserPermission(user, PermissionLevel.COMMON)) {
            return ResponseConstant.X_USER_FORBIDDEN;
        }
        userService.saveUserToSession(request.getSession(), user);
        userService.userAccess(username, IpUtil.getIpAddr(request));
        return ResponseConstant.V_SSO_LOGIN_SUCCESS;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Map<String, Object> logout(HttpServletRequest request) {
        userService.deleteUserFromSession(request.getSession());
        return ResponseConstant.V_USER_LOGOUT_SUCCESS;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> get(HttpServletRequest request) {
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }
        user.setPassword("");
        userService.userAccess(user.getUsername(), IpUtil.getIpAddr(request));
        return BaseResponsePackageUtil.baseData(user);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "nickname", required = true) String nickname,
            @RequestParam(name = "password", required = true) String password,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "school", required = false) String school,
            @RequestParam(name = "captcha", required = true) String captcha,
            HttpServletRequest request
    ) {
        if (!captchaService.checkCaptcha(request.getSession(), captcha)) {
            return ResponseConstant.X_CAPTCHA_WRONG;
        }
        if (userService.hasUser(username)) {
            return ResponseConstant.X_USER_ALREADY_EXISTS;
        }
        userService.newUser(username, nickname, password, email, school);
        return ResponseConstant.V_USER_REGISTER_SUCCESS;
    }

    @RequestMapping(value = "/rank", method = RequestMethod.GET)
    public Map<String, Object> getRank(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "pageSize", required = true) int pageSize
    ) {
        int begin = (page - 1) * pageSize;
        List<UserRank> userRanks = userService.getUserRank(page, pageSize);
        for (UserRank rank : userRanks) {
            rank.setRank(++begin);
        }
        return BaseResponsePackageUtil.baseData(ImmutableMap.of(
                "users", userRanks,
                "count", userService.getUserCount(),
                "page", page,
                "pageSize", pageSize
        ));
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Map<String, Object> getUserInfo(
            @RequestParam(name = "username", required = true) String username
    ) {
        return BaseResponsePackageUtil.baseData(userService.getUserInfo(username));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public Map<String, Object> getUserEdit(
            @RequestParam(name = "username", required = true) String username,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }
        if (!(user.isAdmin() || user.getUsername().equals(username))) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        UserEdit userEdit = userService.getUserEdit(username);
        if (userEdit == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        userEdit.setPassword("");
        return BaseResponsePackageUtil.baseData(userEdit);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateUserEdit(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password,
            @RequestParam(name = "nickname", required = false) String nickname,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "school", required = false) String school,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        if ((user == null) || !(user.isAdmin() || user.getUsername().equals(username))) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        UserEdit userEdit = userService.getUserEdit(username);
        if (userEdit == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (!userService.checkUser(username, userEdit.getPassword(), password) && !user.isAdmin()) {
            return ResponseConstant.X_USER_WRONG_PASSWORD;
        }
        if (nickname != null) {
            userEdit.setNickname(nickname);
        }
        if (email != null) {
            userEdit.setEmail(email);
        }
        if (signature != null) {
            userEdit.setSignature(signature);
        }
        if (school != null) {
            userEdit.setSchool(school);
        }
        if (newPassword != null) {
            userEdit.setPassword(userService.generateUserPassword(username, newPassword));
        }
        userService.updateUserEdit(userEdit);
        if (nickname != null) {
            user.setNickname(nickname);
            userService.saveUserToSession(request.getSession(), user);
        }
        return ResponseConstant.V_UPDATE_SUCCESS;
    }

    @RequestMapping(value = "/full", method = RequestMethod.GET)
    public Map<String, Object> getFullInfo(
            @RequestParam(value = "username", required = true) String username,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        UserFull user = userService.getUserFullByUsername(username);
        if (user == null) {
            return ResponseConstant.X_USER_NOT_FOUND;
        }
        return BaseResponsePackageUtil.baseData(user);
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public Map<String, Object> changeStatus(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "status", required = true) int status,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        if (!userService.checkUserPermission(user, PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        if (user.getUsername().equals(username)) {
            return ResponseConstant.X_CUT_HAIR_BY_SELF;
        }

        user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseConstant.X_USER_NOT_FOUND;
        }
        userService.updateUserStatus(username, status);
        return ResponseConstant.V_UPDATE_SUCCESS;
    }

    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public Map<String, Object> getUserWithPermission(
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.baseData(userService.getUserWithPermission());
    }


}
