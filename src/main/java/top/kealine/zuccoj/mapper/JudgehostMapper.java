package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.kealine.zuccoj.entity.Judgehost;

import java.util.List;

@Mapper
public interface JudgehostMapper {

    @Select("SELECT judgehost_username judgehostUsername, judgehost_password judgehostPassword FROM judgehost WHERE judgehost_username=#{name}")
    Judgehost getJudgehostByName(String name);


    @Select("SELECT judgehost_username judgehostUsername, judgehost_password judgehostPassword FROM judgehost")
    List<Judgehost> getJudgehostList();

    @Insert("INSERT INTO judgehost(judgehost_username, judgehost_password) VALUES(#{name}, #{password})")
    void newJudgehost(String name, String password);

    @Insert("INSERT INTO judgehost_log(judgehost_username, source_ip, description) VALUES(#{name}, #{ip}, #{des})")
    void log(String name, String ip, String des);
}
