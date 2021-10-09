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

    public Problem getProblem(int problemId) {
        return problemMapper.getProblemById(problemId);
    }

    public int updateProblem(Problem problem) {
        return problemMapper.updateProblem(problem);
    }

    public int updateProblemVisible(int problemId, boolean visible) {
        return problemMapper.updateProblemVisible(problemId, visible);
    }

    public List<ProblemInfo> getProblemInfoList(int page, int pageSize, boolean showAll, String username) {
        return problemMapper.getProblemInfoList((page-1)*pageSize, pageSize, showAll, username);
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

    public String getProblemTitle(int problemId) {
        return problemMapper.getProblemTitle(problemId);
    }

    public List<ProblemInfo> searchProblemInfo(int page, int pageSize,boolean showAll, String keyWord, String userName){
        return problemMapper.searchProblemInfo((page-1)*pageSize,pageSize,showAll,keyWord,userName);
    }

    public int getSearchProblemInfoCount(boolean showAll,String keyWord){
        return problemMapper.getSearchProblemInfoCount(showAll,keyWord);
    }
}
