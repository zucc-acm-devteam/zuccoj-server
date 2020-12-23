package top.kealine.zuccoj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.kealine.zuccoj.config.OnlineJudgeConfig;

import java.io.File;

@Component
public class OnlineJudgeInitializer implements ApplicationRunner {
    @Autowired
    private OnlineJudgeConfig config;

    private void fileDirCheck() {
        File dir = new File(config.uploadRoot);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File dataDir = new File(config.dataDir);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        File fileDir = new File(config.filesDir);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File tempDir = new File(config.tempDir);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        fileDirCheck();
    }
}
