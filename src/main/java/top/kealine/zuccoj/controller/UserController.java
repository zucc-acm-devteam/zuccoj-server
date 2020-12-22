package top.kealine.zuccoj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
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
}
