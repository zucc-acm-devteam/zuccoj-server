package top.kealine.fileserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import top.kealine.fileserver.entity.Testcase;

import java.util.List;

@Mapper
public interface TestcaseMapper {
    @Results({
            @Result(column = "testcase_id", property = "testcaseId"),
            @Result(column = "problem_id", property = "problemId"),
            @Result(column = "input_filename", property = "inputFilename"),
            @Result(column = "output_filename", property = "outputFilename"),
            @Result(column = "input_MD5", property = "inputMD5"),
            @Result(column = "output_MD5", property = "outputMD5")
    })
    @Select("SELECT * FROM testcases WHERE problem_id=#{problemId}")
    List<Testcase> getTestcaseByProblemId(int problemId);
}
