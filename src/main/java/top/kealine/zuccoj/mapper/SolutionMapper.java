package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.JudgeTask;
import top.kealine.zuccoj.entity.Solution;
import top.kealine.zuccoj.entity.SolutionResult;
import top.kealine.zuccoj.entity.SolutionStatus;

import java.util.List;

@Mapper
public interface SolutionMapper {
    @Insert("INSERT INTO solutions(problem_id, username, code, result, code_length, lang) VALUES(#{problemId}, #{username}, #{code}, #{result}, #{codeLength}, #{lang})")
    @Options(useGeneratedKeys = true, keyProperty = "solutionId")
    void newSolution(Solution solution);

    @Select("SELECT solution_id solutionId, problem_id problemId, username, code, submit_time submitTime, result, " +
            "memory_used memoryUsed, time_used timeUsed, code_length codeLength, lang, remark, judgehost " +
            "FROM solutions WHERE solution_id = #{solutionId}")
    Solution getSolutionById(long solutionId);


    @Select("SELECT solution_id solutionId, problem_id problemId, username, submit_time submitTime, result, " +
            "memory_used memoryUsed, time_used timeUsed, code_length codeLength, lang " +
            "FROM solutions\n" +
            "ORDER BY solution_id DESC LIMIT #{offset}, #{size}")
    List<SolutionStatus> getSolutionStatus(int offset, int size);

    @Select("SELECT solution_id solutionId, result, memory_used memoryUsed, time_used timeUsed, remark, judgehost FROM solutions WHERE solution_id = #{solutionId}")
    SolutionResult getSolutionResultById(long solutionId);

    @Select("SELECT time_limit timeLimit, memory_limit memoryLimit, solutionId, code, lang, problemId FROM problems " +
            "JOIN (SELECT solution_id solutionId, code, lang, solutions.problem_id problemId FROM solutions WHERE solution_id = #{solutionId}) solution " +
            "ON problems.problem_id = solution.problemId")
    JudgeTask generateJudgeTask(long solutionId);

    @Update("UPDATE solutions SET result=#{result}, memory_used=#{memoryUsed}, time_used=#{timeUsed}, remark=#{remark}, judgehost=#{judgehost} WHERE solution_id=#{solutionId}")
    void updateSolutionResult(SolutionResult solutionResult);
}
