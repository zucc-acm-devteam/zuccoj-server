package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.constant.JudgeResult;
import top.kealine.zuccoj.entity.Solution;
import top.kealine.zuccoj.entity.SolutionResult;
import top.kealine.zuccoj.entity.SolutionStatus;
import top.kealine.zuccoj.mapper.SolutionMapper;

import java.util.List;

@Service
public class SolutionService {

    private final SolutionMapper solutionMapper;

    @Autowired
    SolutionService(SolutionMapper solutionMapper) {
        this.solutionMapper = solutionMapper;
    }

    public long newSolution(int problemId, String username, String code, int lang) {
        Solution solution = new Solution();
        solution.setProblemId(problemId);
        solution.setUsername(username);
        solution.setCode(code);
        solution.setLang(lang);
        solution.setResult(JudgeResult.PENDING);
        solution.setCodeLength(code.length());
        solutionMapper.newSolution(solution);
        return solution.getSolutionId();
    }

    public Solution getSolutionById(long solutionId) {
        return solutionMapper.getSolutionById(solutionId);
    }

    public SolutionResult getSolutionResultById(long solutionId) {
        return solutionMapper.getSolutionResultById(solutionId);
    }

    public List<SolutionStatus> getSolutionStatus(int offset, int size) {
        return solutionMapper.getSolutionStatus(offset, size);
    }
}
