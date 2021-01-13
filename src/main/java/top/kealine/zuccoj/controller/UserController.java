package top.kealine.zuccoj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.service.CaptchaService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
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
        user.setPassword("");
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

}
