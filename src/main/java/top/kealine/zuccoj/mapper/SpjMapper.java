package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.kealine.zuccoj.entity.Spj;

@Mapper
public interface SpjMapper {
    @Select("SELECT problem_id problemId, spj_code spjCode FROM spj WHERE problem_id = #{proId}")
    Spj getSpj(int proId);

    @Insert("INSERT spj(problem_id, spj_code) VALUES(#{problemId}, #{spjCode})")
    void newSpj(Spj spj);
}
