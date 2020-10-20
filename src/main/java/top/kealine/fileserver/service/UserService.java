package top.kealine.fileserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.fileserver.entity.User;
import top.kealine.fileserver.mapper.UserMapper;

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
}
