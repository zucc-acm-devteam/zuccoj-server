package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import top.kealine.zuccoj.entity.Solution;

@Mapper
public interface SolutionMapper {
    @Insert("INSERT INTO solutions(problem_id, username, code, result, code_length, lang) VALUES(#{problemId}, #{username}, #{code}, #{result}, #{code_length}, #{lang})")
    @Options(useGeneratedKeys = true, keyProperty = "solutionId")
    void newSolution(Solution solution);
}
