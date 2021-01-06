package top.kealine.zuccoj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.constant.JudgeResult;
import top.kealine.zuccoj.entity.JudgeTask;
import top.kealine.zuccoj.entity.Solution;
import top.kealine.zuccoj.entity.SolutionResult;
import top.kealine.zuccoj.entity.SolutionStatus;
import top.kealine.zuccoj.mapper.SolutionMapper;
import top.kealine.zuccoj.mapper.TestcaseMapper;

import java.util.List;

@Service
public class SolutionService {
    private static final String JUDGE_TASK_QUEUE_KEY = "ZUCCOJ::JUDGE::TASK::QUEUE";
    private final RedisTemplate<String, String> redisTemplate;
    private final SolutionMapper solutionMapper;
    private final TestcaseMapper testcaseMapper;

    @Autowired
    SolutionService(
            RedisTemplate<String,String> redisTemplate,
            SolutionMapper solutionMapper,
            TestcaseMapper testcaseMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.solutionMapper = solutionMapper;
        this.testcaseMapper = testcaseMapper;
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

    public JudgeTask generateJudgeTask(long solutionId) {
        JudgeTask judgeTask = solutionMapper.generateJudgeTask(solutionId);
        List<Integer> testcaseList = testcaseMapper.getTestcaseIdByProblemId(judgeTask.getProblemId());
        judgeTask.setTestcaseList(testcaseList);
        return judgeTask;
    }

    public void publishTask(long solutionId) throws JsonProcessingException {
        JudgeTask judgeTask = generateJudgeTask(solutionId);
        String judgeTaskString = new ObjectMapper().writeValueAsString(judgeTask);
        ListOperations<String,String> ops = redisTemplate.opsForList();
        ops.leftPush(JUDGE_TASK_QUEUE_KEY, judgeTaskString);
    }
}
