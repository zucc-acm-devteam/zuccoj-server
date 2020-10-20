package top.kealine.filesever.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.filesever.entity.User;
import top.kealine.filesever.service.UserService;
import top.kealine.filesever.util.BaseResponsePackageUtil;

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
            return BaseResponsePackageUtil.errorMessage(String.format("User %s Don't Exist.", username));
        }
        if (!userService.checkUser(user, password)) {
            return BaseResponsePackageUtil.errorMessage("Wrong Password");
        }
        // FileSever User Must Be Admin
        if (!user.isAdmin()) {
            return BaseResponsePackageUtil.errorMessage("Access Denied");
        }
        userService.saveUserToSession(request.getSession(), user);
        user.setPassword("");
        return BaseResponsePackageUtil.baseData(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Map<String, Object> logout(HttpServletRequest request) {
        userService.deleteUserFromSession(request.getSession());
        return BaseResponsePackageUtil.succeedMessage("Logout Successfully");
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> get(HttpServletRequest request) {
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return BaseResponsePackageUtil.errorMessage("Please Login First");
        }
        user.setPassword("");
        return BaseResponsePackageUtil.baseData(user);
    }
}
