package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.kealine.zuccoj.entity.User;

@Mapper
public interface UserMapper {
    @Select("SELECT username, nickname, password, status FROM users WHERE username=#{username}")
    User getUserByUsername(String username);

    @Select("SELECT COUNT(*) FROM users WHERE username=#{username}")
    int countUserByUsername(String username);

    @Insert("INSERT INTO users(username, nickname, password, status, email, school) VALUES(#{username}, #{nickname}, #{password}, #{status}, #{email}, #{school})")
    int newUser(User user);
}
