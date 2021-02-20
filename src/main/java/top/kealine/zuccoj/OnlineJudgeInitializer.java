package top.kealine.zuccoj;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.kealine.zuccoj.config.OnlineJudgeConfig;
import top.kealine.zuccoj.constant.JudgeResult;
import top.kealine.zuccoj.service.SolutionService;

import java.io.File;
import java.util.List;

@Component
public class OnlineJudgeInitializer implements ApplicationRunner {
    private final OnlineJudgeConfig config;
    private final SolutionService solutionService;

    @Autowired
    OnlineJudgeInitializer(
            OnlineJudgeConfig config,
            SolutionService solutionService
    ) {
        this.config = config;
        this.solutionService = solutionService;
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

    private void rejudgePendingSolution() throws JsonProcessingException {
        List<Long> pendingTasks = this.solutionService.getAllSolutionWithResult(JudgeResult.PENDING);
        for (Long solutionId: pendingTasks) {
            this.solutionService.publishTask(solutionId);
        }
    }

    private void rejudgeSystemErrorSolution()  throws JsonProcessingException {
        List<Long> seTasks = this.solutionService.getAllSolutionWithResult(JudgeResult.SYSTEM_ERROR);
        for (Long solutionId: seTasks) {
            this.solutionService.rejudgeSolution(solutionId);
        }
    }

    private void initializeTaskQueue() throws Exception{
        this.solutionService.cancelAllTask();
        this.rejudgePendingSolution();
        this.rejudgeSystemErrorSolution();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        fileDirCheck();
        initializeTaskQueue();
    }
}
