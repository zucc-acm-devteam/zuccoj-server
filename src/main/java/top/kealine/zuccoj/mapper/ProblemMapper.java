package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.Problem;
import top.kealine.zuccoj.entity.ProblemDisplay;
import top.kealine.zuccoj.entity.ProblemInAdmin;
import top.kealine.zuccoj.entity.ProblemInfo;

import java.util.List;

@Mapper
public interface ProblemMapper {
    @Select("SELECT COUNT(*) FROM problems")
    int getProblemCount();

    @Select("SELECT \n" +
            "problems.problem_id problemId, title, time_limit timeLimit,memory_limit memoryLimit, visible, t1.cnt testcaseCnt\n" +
            "FROM problems LEFT JOIN\n" +
            "(SELECT\n" +
            "testcases.problem_id, COUNT(1) cnt\n" +
            "FROM\n" +
            "testcases\n" +
            "GROUP BY \n" +
            "testcases.problem_id) t1\n" +
            "ON problems.problem_id = t1.problem_id\n" +
            "LIMIT #{offset}, #{size}")
    List<ProblemInAdmin> getProblemListForAdmin(int offset, int size);


    @Insert("INSERT INTO problems(title, description, input, output, hint, time_limit, memory_limit, spj, visible, samples, tags) " +
            "VALUES(#{title}, #{description}, #{input}, #{output}, #{hint}, #{timeLimit}, #{memoryLimit}, #{spj}, #{visible}, #{samples}, #{tags})")
    @Options(useGeneratedKeys = true, keyProperty = "problemId")
    void newProblem(Problem problem);

    @Select("SELECT problem_id problemId, title, description, input, output, hint, time_limit timeLimit, memory_limit memoryLimit, \n" +
            "spj, visible, samples, tags FROM problems WHERE problem_id=#{problemId}")
    Problem getProblemById(int problemId);


    @Update("UPDATE problems SET title=#{title}, description=#{description}, input=#{input}, output=#{output}, hint=#{hint}, time_limit=#{timeLimit}, " +
            "memory_limit=#{memoryLimit}, spj=#{spj}, samples=#{samples}, tags=#{tags} WHERE problem_id=#{problemId}")
    int updateProblem(Problem problem);

    @Update("UPDATE problems SET visible=#{visible} WHERE problem_id=#{problemId}")
    int updateProblemVisible(int problemId, boolean visible);

    @Select("<script> " +
            "SELECT problems.problem_id problemId, title, time_limit timeLimit, memory_limit memoryLimit, tags,visible, \n" +
            "(SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id) submitted, \n" +
            "(SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id AND solutions.result = 7) solved\n" +
            "<if test=\"username != null\"> \n" +
            ",( " +
            "IF ((SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id AND solutions.username = #{username})=0,0,\n" +
            "(IF ((SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id AND solutions.result = 7 AND solutions.username = #{username})>0,1,-1)))\n" +
            ") `status`\n" +
            "</if> \n" +
            "FROM problems \n" +
            "WHERE 1=1\n" +
            "<if test=\"showAll != true\"> AND problems.visible = true </if> \n" +
            "ORDER BY problemId ASC\n" +
            "LIMIT #{offset}, #{size} \n" +
            "</script>")
    List<ProblemInfo> getProblemInfoList(int offset, int size, boolean showAll, String username);

    @Select("<script> " +
            "SELECT COUNT(*)" +
            "FROM problems \n" +
            "WHERE 1=1\n" +
            "<if test=\"showAll != true\"> AND problems.visible = true </if> \n" +
            "</script>")
    int getProblemInfoListCount(boolean showAll);

    @Select("SELECT problems.problem_id problemId, title, time_limit timeLimit, memory_limit memoryLimit, tags, visible, \n" +
            "(SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id) submitted, \n" +
            "(SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id AND solutions.result = 7) solved\n" +
            "FROM problems \n" +
            "WHERE problems.problem_id = #{problemId}")
    ProblemInfo getProblemInfo(int problemId);


    @Select("SELECT problem_id problemId, title, time_limit timeLimit, memory_limit memoryLimit, description, input, output, hint, samples, visible \n" +
            "FROM problems WHERE problem_id = #{problemId}")
    ProblemDisplay getProblemDisplay(int problemId);

    @Select("SELECT title FROM problems WHERE problem_id = #{problemId}")
    String getProblemTitle(int problemId);

    @Select("<script>" +
            "select problems.problem_id problemId, title, time_limit timeLimit, memory_limit memoryLimit, tags, visible, \n" +
            "(SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id) submitted, \n" +
            "(SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id AND solutions.result = 7) solved\n" +
            "<if test=\"username != null\"> \n" +
            ",( " +
            "IF ((SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id AND solutions.username = #{username})=0,0,\n" +
            "(IF ((SELECT COUNT(*) FROM solutions WHERE solutions.problem_id = problems.problem_id AND solutions.result = 7 AND solutions.username = #{username})>0,1,-1)))\n" +
            ") `status`\n" +
            "</if> \n"+
            "FROM problems \n" +
            "WHERE 1=1\n" +
            "<if test=\"showAll != true\"> AND problems.visible = true </if> \n" +
            "and problems.title like binary '%${keyWord}%' or tags like binary '%${keyWord}%' \n"+
            "ORDER BY problemId ASC\n" +
            "LIMIT #{offset}, #{size} \n" +
            "</script>")
    List<ProblemInfo> searchProblemInfo(int offset, int size,boolean showAll, String keyWord, String username);

    @Select("<script>"+
            "select count(*) from problems where 1 = 1"+
            "<if test=\"showAll != true\"> AND problems.visible = true </if> \n" +
            "and problems.title like binary '%${keyWord}%' or tags like binary '%${keyWord}%' \n"+
            "</script>")
    int getSearchProblemInfoCount(boolean showAll, String keyWord);
}
