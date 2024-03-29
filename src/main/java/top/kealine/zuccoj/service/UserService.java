package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.entity.UserEdit;
import top.kealine.zuccoj.entity.UserFull;
import top.kealine.zuccoj.entity.UserInfo;
import top.kealine.zuccoj.entity.UserRank;
import top.kealine.zuccoj.mapper.UserMapper;
import top.kealine.zuccoj.util.PasswordUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public String generateUserPassword(String username, String password) {
        return PasswordUtil.encrypt(username+password);
    }

    public boolean checkUser(User user, String userInput) {
        return checkUser(user.getUsername(), user.getPassword(), userInput);
    }

    public boolean checkUser(String username, String password, String userInput) {
        return PasswordUtil.check(password, username+userInput);
    }

    public boolean hasUser(String username) {
        return userMapper.countUserByUsername(username) > 0;
    }

    public void newUser(String username, String nickname, String password, String email, String school) {
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(generateUserPassword(username, password));
        user.setEmail(email);
        user.setSchool(school);
        user.setStatus(PermissionLevel.COMMON);
        userMapper.newUser(user);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public int getUserCount() {
        return userMapper.getUserCount();
    }

    public List<UserRank> getUserRank(int page, int pageSize) {
        return userMapper.getUserRank((page-1)*pageSize, pageSize);
    }

    public UserInfo getUserInfo(String username) {
        return userMapper.getUserInfo(username);
    }

    public UserEdit getUserEdit(String username) {
        return userMapper.getUserEdit(username);
    }

    public void updateUserEdit(UserEdit userEdit) {
        userMapper.updateUserEdit(userEdit);
    }

    public void userAccess(String username, String ip) {
        userMapper.userAccess(username, ip);
    }

    public void updateUserStatus(String username, int status) {
        userMapper.updateUserStatus(username, status);
    }

    public UserFull getUserFullByUsername(String username) {
        UserFull userFull = userMapper.getUserFullByUsername(username);
        if (userFull == null) {
            return null;
        }
        userFull.setStatusText(PermissionLevel.toText(userFull.getStatus()));
        return userFull;
    }

    public List<UserFull> getUserWithPermission() {
        List<UserFull> userFulls = userMapper.getUserWithPermission();
        userFulls.forEach(
                userFull -> {
                    userFull.setStatusText(PermissionLevel.toText(userFull.getStatus()));
                }
        );
        return userFulls;
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
        if (permissionLevel == PermissionLevel.ANYONE) {
            return true;
        }
        if (user == null) {
            return false;
        }
        return user.getStatus() >= permissionLevel;
    }
}
