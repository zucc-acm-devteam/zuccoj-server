package top.kealine.fileserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.kealine.fileserver.entity.Problem;

import java.util.List;

@Mapper
public interface ProblemMapper {
    @Select("SELECT COUNT(*) FROM problems")
    int getProblemCount();

    @Select("SELECT problem_id problemId, title FROM problems LIMIT #{offset}, #{size}")
    List<Problem> getProblemByPaging(int offset, int size);
}
