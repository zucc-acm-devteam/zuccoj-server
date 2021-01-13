package top.kealine.zuccoj.entity;

import top.kealine.zuccoj.constant.PermissionLevel;

public class User {
    private String username;
    private String nickname;
    private String password;
    private int status;
    private String email;
    private String school;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status > PermissionLevel.FORBIDDEN;
    }

    public boolean isAdmin() {
        return status >= PermissionLevel.ADMIN;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
