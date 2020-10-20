package top.kealine.fileserver.entity;

public class User {
    private String username;
    private String nickname;
    private String password;
    private int status;

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
        return status > 0;
    }

    public boolean isAdmin() {
        return status == 999;
    }

}
