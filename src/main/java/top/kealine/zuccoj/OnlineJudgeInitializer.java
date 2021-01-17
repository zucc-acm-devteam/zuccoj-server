package top.kealine.zuccoj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.kealine.zuccoj.config.OnlineJudgeConfig;

import java.io.File;

@Component
public class OnlineJudgeInitializer implements ApplicationRunner {
    private final OnlineJudgeConfig config;

    @Autowired
    OnlineJudgeInitializer(OnlineJudgeConfig config) {
        this.config = config;
    }

    private void clearTempDir(File tempDir) throws Exception {
        File[] files = tempDir.listFiles();
        if (files != null) {
            for (File file: files) {
                if (file.isDirectory()) {
                    clearTempDir(file);
                }
                if (!file.delete()) {
                    throw new Exception(String.format("cannot delete temp file %s", file.getAbsolutePath()));
                }
            }
        }
    }

    private void fileDirCheck() throws Exception {
        File dir = new File(config.uploadRoot);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new Exception("cannot create uploadRoot");
            }
        }
        File dataDir = new File(config.dataDir);
        if (!dataDir.exists()) {
            if (!dataDir.mkdir()) {
                throw new Exception("cannot create dataDir");
            }
        }
        File fileDir = new File(config.filesDir);
        if (!fileDir.exists()) {
            if (!fileDir.mkdir()) {
                throw new Exception("cannot create fileDir");
            }
        }
        File tempDir = new File(config.tempDir);
        if (!tempDir.exists()) {
            if (!tempDir.mkdir()) {
                throw new Exception("cannot create tempDir");
            }
        }
        clearTempDir(tempDir);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        fileDirCheck();
    }
}
