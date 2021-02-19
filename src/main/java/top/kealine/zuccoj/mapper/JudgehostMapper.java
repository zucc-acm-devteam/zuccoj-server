package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.Judgehost;

import java.util.List;

@Mapper
public interface JudgehostMapper {
    @Select("SELECT judgehost_username judgehostUsername, judgehost_password judgehostPassword FROM judgehost WHERE judgehost_username=#{name}")
    Judgehost getJudgehostByName(String name);

    @Select("SELECT judgehost_username judgehostUsername, judgehost_password judgehostPassword FROM judgehost ORDER BY create_time ASC")
    List<Judgehost> getJudgehostList();

    @Insert("INSERT INTO judgehost(judgehost_username, judgehost_password) VALUES(#{name}, #{password})")
    void newJudgehost(String name, String password);

    @Delete("DELETE FROM judgehost WHERE judgehost_username=#{name}")
    void deleteJudgehost(String name);

    @Update("UPDATE judgehost SET judgehost_password=#{password} WHERE judgehost_username=#{name}")
    void updateJudgehost(String name, String password);

    @Insert("INSERT INTO judgehost_log(judgehost_username, source_ip, description) VALUES(#{name}, #{ip}, #{des})")
    void log(String name, String ip, String des);
}
