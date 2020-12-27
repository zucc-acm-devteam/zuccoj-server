package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.kealine.zuccoj.entity.Problem;
import top.kealine.zuccoj.entity.ProblemInAdmin;

import java.util.List;

@Mapper
public interface ProblemMapper {
    @Select("SELECT COUNT(*) FROM problems")
    int getProblemCount();

    @Select("SELECT \n" +
            "problems.problem_id problemId, title, time_limit timeLimit,memory_limit memoryLimit, t1.cnt testcaseCnt\n" +
            "FROM problems LEFT JOIN\n" +
            "(SELECT\n" +
            "testcases.problem_id, COUNT(1) cnt\n" +
            "FROM\n" +
            "testcases\n" +
            "GROUP BY \n" +
            "testcases.problem_id) t1\n" +
            "ON problems.problem_id = t1.problem_id\n" +
            "LIMIT #{offset}, #{size}")
    List<ProblemInAdmin> getProblemByPaging(int offset, int size);


    @Insert("INSERT INTO problems(title, description, input, output, hint, time_limit, memory_limit, spj, visible, samples, tags) " +
            "VALUES(#{title}, #{description}, #{input}, #{output}, #{hint}, #{timeLimit}, #{memoryLimit}, #{spj}, #{visible}, #{samples}, #{tags})")
    @Options(useGeneratedKeys = true, keyProperty = "problemId")
    void newProblem(Problem problem);
}
