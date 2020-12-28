package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.config.OnlineJudgeConfig;
import top.kealine.zuccoj.entity.Problem;
import top.kealine.zuccoj.entity.ProblemDisplay;
import top.kealine.zuccoj.entity.ProblemInAdmin;
import top.kealine.zuccoj.entity.ProblemInfo;
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

    public List<ProblemInAdmin> getProblemListForAdmin(int page, int pageSize) {
        return problemMapper.getProblemListForAdmin((page-1)*pageSize, pageSize);
    }

    public boolean newProblemDataDir(int id) {
        return new File(onlineJudgeConfig.dataDir + id + onlineJudgeConfig.separator).mkdir();
    }

    public int newProblem(Problem problem) {
        problemMapper.newProblem(problem);
        newProblemDataDir(problem.getProblemId());
        return problem.getProblemId();
    }

    public List<ProblemInfo> getProblemInfoList(int page, int pageSize, boolean showAll) {
        return problemMapper.getProblemInfoList((page-1)*pageSize, pageSize, showAll);
    }

    public int getProblemInfoListCount(boolean showAll) {
        return problemMapper.getProblemInfoListCount(showAll);
    }

    public ProblemInfo getProblemInfo(int problemId) {
        return problemMapper.getProblemInfo(problemId);
    }

    public ProblemDisplay getProblemDisplay(int problemId) {
        return problemMapper.getProblemDisplay(problemId);
    }
}
