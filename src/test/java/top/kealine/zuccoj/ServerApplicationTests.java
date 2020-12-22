package top.kealine.zuccoj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.kealine.zuccoj.config.OnlineJudgeConfig;
import top.kealine.zuccoj.service.ProblemService;

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

}
