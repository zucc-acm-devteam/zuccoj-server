package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import top.kealine.zuccoj.entity.Testcase;

import java.util.List;

@Mapper
public interface TestcaseMapper {
    @Results({
            @Result(column = "testcase_id", property = "testcaseId"),
            @Result(column = "problem_id", property = "problemId"),
            @Result(column = "input_filename", property = "inputFilename"),
            @Result(column = "output_filename", property = "outputFilename"),
            @Result(column = "input_MD5", property = "inputMD5"),
            @Result(column = "output_MD5", property = "outputMD5"),
            @Result(column = "input_size", property = "inputSize"),
            @Result(column = "output_size", property = "outputSize"),
    })
    @Select("SELECT * FROM testcases WHERE problem_id=#{problemId}")
    List<Testcase> getTestcaseByProblemId(int problemId);

    @Insert("INSERT INTO testcases(problem_id, input_filename, output_filename, input_MD5, output_MD5, input_size, output_size) VALUES(#{problemId},#{inputFilename},#{outputFilename},#{inputMD5},#{outputMD5},#{inputSize},#{outputSize})")
    @Options(useGeneratedKeys = true, keyProperty = "testcaseId")
    void newTestcase(Testcase testcase);

    @Delete("DELETE FROM testcases WHERE testcase_id=#{id}")
    void delTestcase(int id);

    @Select("SELECT COUNT(*) FROM testcases WHERE testcase_id=#{id}")
    int countTestcaseById(int id);

    @Select("SELECT problem_id FROM testcases WHERE testcase_id=#{testcaseId}")
    int getProblemIdByTestcaseId(int testcaseId);
}
