package top.kealine.fileserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.kealine.fileserver.entity.User;

@Mapper
public interface UserMapper {
    @Select("SELECT username, nickname, password, status FROM users WHERE username=#{username}")
    User getUserByUsername(String username);
}
