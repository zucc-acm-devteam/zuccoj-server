package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.mapper.UserMapper;

import javax.servlet.http.HttpSession;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public boolean checkUser(User user, String password) {
        return user.getPassword().equals(password);
    }

    public void saveUserToSession(HttpSession session, User user) {
        session.setAttribute("username", user.getUsername());
        session.setAttribute("nickname", user.getNickname());
        session.setAttribute("status", user.getStatus());
    }

    public void deleteUserFromSession(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("nickname");
        session.removeAttribute("status");
    }

    public User getUserFromSession(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return null;
        }
        User user = new User();
        user.setUsername(username);
        user.setNickname((String) session.getAttribute("nickname"));
        user.setStatus((int) session.getAttribute("status"));
        return user;
    }

    public boolean checkUserPermission(HttpSession session, int permissionLevel) {
        if (permissionLevel == PermissionLevel.ANYONE) {
            return true;
        }
        User user = getUserFromSession(session);
        return checkUserPermission(user, permissionLevel);
    }

    public boolean checkUserPermission(User user, int permissionLevel) {
        if (user == null) {
            return false;
        }
        return user.getStatus() >= permissionLevel;
    }
}
