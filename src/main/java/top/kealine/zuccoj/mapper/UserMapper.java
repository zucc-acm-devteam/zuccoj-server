package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.entity.UserEdit;
import top.kealine.zuccoj.entity.UserFull;
import top.kealine.zuccoj.entity.UserInfo;
import top.kealine.zuccoj.entity.UserRank;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT username, nickname, password, status FROM users WHERE username=#{username}")
    User getUserByUsername(String username);

    @Select("SELECT COUNT(*) FROM users WHERE username=#{username}")
    int countUserByUsername(String username);

    @Insert("INSERT INTO users(username, nickname, password, status, email, school) VALUES(#{username}, #{nickname}, #{password}, #{status}, #{email}, #{school})")
    int newUser(User user);

    @Select("SELECT COUNT(*) FROM users")
    int getUserCount();

    @Select("SELECT username, nickname, signature, \n" +
            "(SELECT COUNT(problem_id) FROM solutions WHERE username=users.username) submitted, \n" +
            "(SELECT COUNT(problem_id) FROM solutions WHERE result=7 AND username=users.username) solved \n" +
            "FROM users ORDER BY solved DESC, submitted DESC \n" +
            "LIMIT #{offset}, #{size}")
    List<UserRank> getUserRank(int offset, int size);

    @Select("SELECT username, nickname, reg_time regTime, school, signature, \n" +
            "(SELECT COUNT(problem_id) FROM solutions WHERE username=users.username) submitted, \n" +
            "(SELECT COUNT(DISTINCT problem_id) FROM solutions WHERE result=7 AND username=users.username) solved \n" +
            "FROM users WHERE username=#{username}")
    UserInfo getUserInfo(String username);

    @Select("SELECT username, nickname, email, signature, school, password FROM users WHERE username=#{username}")
    UserEdit getUserEdit(String username);

    @Update("UPDATE users SET nickname=#{nickname}, email=#{email}, signature=#{signature}, school=#{school}, password=#{password} WHERE username=#{username}")
    void updateUserEdit(UserEdit userEdit);

    @Update("UPDATE users SET ip=#{ip},access_time=NOW() WHERE username=#{username}")
    void userAccess(String username, String ip);

    @Update("UPDATE users SET status=#{status} WHERE username=#{username}")
    void updateUserStatus(String username, int status);

    @Select("SELECT username, email, ip, access_time accessTime, reg_time regTime, nickname, school, status, signature FROM users WHERE username=#{username}")
    UserFull getUserFullByUsername(String username);

    @Select("SELECT username, email, ip, access_time accessTime, reg_time regTime, nickname, school, status, signature FROM users WHERE status > 0")
    List<UserFull> getUserWithPermission();
}
