package top.kealine.zuccoj;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.kealine.zuccoj.config.OnlineJudgeConfig;
import top.kealine.zuccoj.mapper.TestcaseMapper;
import top.kealine.zuccoj.service.ProblemService;
import top.kealine.zuccoj.service.SolutionService;

@SpringBootTest
class ServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    OnlineJudgeConfig config;

    @Test
    void fileRootTest() {
        System.out.println(config.uploadRoot);
        System.out.println(config.separator);
    }

    @Autowired
    ProblemService problemService;

    @Test
    void problemNewDirTest() {
        problemService.newProblemDataDir(1);
    }

    @Autowired
    SolutionService solutionService;

    @Test
    void testTaskPublish() throws JsonProcessingException {
        solutionService.publishTask(15);
    }

    @Autowired
    TestcaseMapper testcaseMapper;
    @Test
    void caseSumScoreTest() {
        testcaseMapper.countTestcaseById(1001);
//        System.out.println(testcaseMapper.getTestcaseScoreFromList(ImmutableList.of(19, 20)));
    }
}
