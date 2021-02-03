package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.DateCount;
import top.kealine.zuccoj.entity.JudgeTask;
import top.kealine.zuccoj.entity.Solution;
import top.kealine.zuccoj.entity.SolutionResult;
import top.kealine.zuccoj.entity.SolutionStatus;

import java.util.List;

@Mapper
public interface SolutionMapper {
    @Insert("INSERT INTO solutions(problem_id, username, code, result, code_length, lang, contest_id) VALUES(#{problemId}, #{username}, #{code}, #{result}, #{codeLength}, #{lang}, #{contestId})")
    @Options(useGeneratedKeys = true, keyProperty = "solutionId")
    void newSolution(Solution solution);

    @Select("SELECT solution_id solutionId, problems.problem_id problemId, username, code, submit_time submitTime, result, " +
            "memory_used memoryUsed, time_used timeUsed, code_length codeLength, lang, remark, judgehost, contest_id contestId, " +
            "problems.title problemTitle \n" +
            "FROM solutions JOIN problems ON solutions.problem_id = problems.problem_id WHERE solution_id = #{solutionId}")
    Solution getSolutionById(long solutionId);


    @Select("<script> \n" +
            "SELECT solution_id solutionId, problem_id problemId, username, submit_time submitTime, result, " +
            "memory_used memoryUsed, time_used timeUsed, code_length codeLength, lang \n" +
            "FROM solutions \n" +
            "WHERE 1=1 \n" +
            "<if test=\"problemId != null\"> AND problem_id=#{problemId} </if> \n" +
            "<if test=\"username != null\"> AND username=#{username} </if> \n" +
            "<if test=\"lang != null\"> AND lang=#{lang} </if> \n" +
            "<if test=\"result != null\"> AND result=#{result} </if> \n" +
            "<if test=\"judgehost != null\"> AND judgehost=#{judgehost} </if> \n" +
            "<if test=\"contestId != null\"> AND contest_id=#{contestId} </if> \n" +
            "ORDER BY solution_id DESC LIMIT #{offset}, #{size}\n" +
            "</script>")
    List<SolutionStatus> getSolutionStatus(int offset, int size, Integer problemId, String username, Integer lang, Integer result,  String judgehost, Integer contestId);

    @Select("SELECT solution_id solutionId, result, memory_used memoryUsed, time_used timeUsed, remark, judgehost FROM solutions WHERE solution_id = #{solutionId}")
    SolutionResult getSolutionResultById(long solutionId);

    @Select("SELECT time_limit timeLimit, memory_limit memoryLimit, solutionId, code, lang, problemId, spj FROM problems " +
            "JOIN (SELECT solution_id solutionId, code, lang, solutions.problem_id problemId FROM solutions WHERE solution_id = #{solutionId}) solution " +
            "ON problems.problem_id = solution.problemId")
    JudgeTask generateJudgeTask(long solutionId);

    @Update("UPDATE solutions SET result=#{result}, memory_used=#{memoryUsed}, time_used=#{timeUsed}, remark=#{remark}, judgehost=#{judgehost} WHERE solution_id=#{solutionId}")
    void updateSolutionResult(SolutionResult solutionResult);

    @Select("<script> \n" +
            "SELECT\n" +
            "t.date_tag date, \n" +
            "(SELECT COUNT(*) FROM solutions WHERE DATE(submit_time)=t.date_tag <if test=\"username != null\"> AND username=#{username} </if>) as submitted,\n" +
            "(SELECT COUNT(*) FROM solutions WHERE DATE(submit_time)=t.date_tag AND result=7 <if test=\"username != null\"> AND username=#{username} </if>) as solved\n" +
            "FROM (\n" +
            "SELECT DATE(SUBDATE(NOW(),INTERVAL 0 DAY)) as date_tag UNION ALL\n" +
            "SELECT DATE(SUBDATE(NOW(),INTERVAL 1 DAY)) as date_tag UNION ALL \n" +
            "SELECT DATE(SUBDATE(NOW(),INTERVAL 2 DAY)) as date_tag UNION ALL \n" +
            "SELECT DATE(SUBDATE(NOW(),INTERVAL 3 DAY)) as date_tag UNION ALL \n" +
            "SELECT DATE(SUBDATE(NOW(),INTERVAL 4 DAY)) as date_tag UNION ALL \n" +
            "SELECT DATE(SUBDATE(NOW(),INTERVAL 5 DAY)) as date_tag UNION ALL \n" +
            "SELECT DATE(SUBDATE(NOW(),INTERVAL 6 DAY)) as date_tag\n" +
            ") t\n" +
            "ORDER BY t.date_tag\n" +
            "</script>")
    List<DateCount> getDateCountIn7Days(String username);

    @Select("SELECT DATE(submit_time) as date, COUNT(*) submitted FROM solutions WHERE username=#{username} \n" +
            "AND DATE(submit_time) > SUBDATE(NOW(),INTERVAL 1 YEAR) GROUP BY DATE(submit_time)")
    List<DateCount> getDateCountIn1Year(String username);
}
