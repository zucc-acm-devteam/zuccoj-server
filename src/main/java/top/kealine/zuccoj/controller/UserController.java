package top.kealine.zuccoj.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.entity.UserEdit;
import top.kealine.zuccoj.entity.UserRank;
import top.kealine.zuccoj.service.CaptchaService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;
import top.kealine.zuccoj.util.PasswordUtil;

import javax.servlet.http.HttpServletRequest;
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
        if(!userService.checkUserPermission(user, PermissionLevel.COMMON)) {
            return ResponseConstant.X_USER_FORBIDDEN;
        }
        userService.saveUserToSession(request.getSession(), user);
        return ResponseConstant.V_USER_LOGIN_SUCCESS;
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
        int begin = (page-1)*pageSize;
        List<UserRank> userRanks = userService.getUserRank(page, pageSize);
        for (UserRank rank:userRanks) {
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
        if ((user==null) || !(user.isAdmin() || user.getUsername().equals(username))) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        UserEdit userEdit = userService.getUserEdit(username);
        if (userEdit == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (!userService.checkUser(username, userEdit.getPassword(), password)) {
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
}
