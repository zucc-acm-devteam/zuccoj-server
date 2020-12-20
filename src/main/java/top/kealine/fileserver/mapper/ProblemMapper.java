package top.kealine.fileserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.kealine.fileserver.entity.Problem;

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
    List<Problem> getProblemByPaging(int offset, int size);
}
