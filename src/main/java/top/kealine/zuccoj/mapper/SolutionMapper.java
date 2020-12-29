package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.kealine.zuccoj.entity.Solution;
import top.kealine.zuccoj.entity.SolutionResult;

@Mapper
public interface SolutionMapper {
    @Insert("INSERT INTO solutions(problem_id, username, code, result, code_length, lang) VALUES(#{problemId}, #{username}, #{code}, #{result}, #{codeLength}, #{lang})")
    @Options(useGeneratedKeys = true, keyProperty = "solutionId")
    void newSolution(Solution solution);

    @Select("SELECT solution_id solutionId, problem_id problemId, username, code, submit_time submitTime, result, " +
            "memory_used memoryUsed, time_used timeUsed, code_length codeLength, lang, remark " +
            "FROM solutions WHERE solution_id = #{solutionId}")
    Solution getSolutionById(long solutionId);

    @Select("SELECT solution_id solutionId, result, memory_used memoryUsed, time_used timeUsed, remark FROM solutions WHERE solution_id = #{solutionId}")
    SolutionResult getSolutionResultById(long solutionId);
}
