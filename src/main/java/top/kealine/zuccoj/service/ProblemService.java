package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.config.OnlineJudgeConfig;
import top.kealine.zuccoj.entity.Problem;
import top.kealine.zuccoj.mapper.ProblemMapper;

import java.io.File;
import java.util.List;

@Service
public class ProblemService {

    private final ProblemMapper problemMapper;
    private final OnlineJudgeConfig onlineJudgeConfig;

    @Autowired
    ProblemService(ProblemMapper problemMapper, OnlineJudgeConfig onlineJudgeConfig) {
        this.problemMapper = problemMapper;
        this.onlineJudgeConfig = onlineJudgeConfig;
    }

    public int getProblemsCount() {
        return problemMapper.getProblemCount();
    }

    public List<Problem> getProblemsByPaging(int page, int pageSize) {
        return problemMapper.getProblemByPaging((page-1)*pageSize, pageSize);
    }

    public boolean newProblemDataDir(int id) {
        return new File(onlineJudgeConfig.dataDir + id + onlineJudgeConfig.separator).mkdir();
    }
}
