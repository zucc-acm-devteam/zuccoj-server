package top.kealine.zuccoj.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.entity.Contest;
import top.kealine.zuccoj.entity.ContestInfo4Scoreboard;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.Solution4Scoreboard;
import top.kealine.zuccoj.mapper.ContestMapper;
import top.kealine.zuccoj.mapper.ScoreboardMapper;
import top.kealine.zuccoj.structure.scoreboard.Scoreboard;
import top.kealine.zuccoj.structure.scoreboard.ScoreboardCalculator;
import top.kealine.zuccoj.structure.scoreboard.ScoreboardCalculatorICPC;
import top.kealine.zuccoj.structure.scoreboard.ScoreboardCalculatorIOI;
import top.kealine.zuccoj.structure.scoreboard.ScoreboardCalculatorOI;

import java.util.List;

import static top.kealine.zuccoj.constant.ContestType.ICPC;
import static top.kealine.zuccoj.constant.ContestType.IOI;
import static top.kealine.zuccoj.constant.ContestType.OI;

@Service
public class ScoreboardRunner {
    private final String REDIS_SCOREBOARD_KEY = "ZUCCOJ::SCOREBOARD";
    private final ScoreboardMapper scoreboardMapper;
    private final ContestMapper contestMapper;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    ScoreboardRunner(
            ScoreboardMapper scoreboardMapper,
            ContestMapper contestMapper,
            ObjectMapper objectMapper,
            RedisTemplate<String, String> redisTemplate
    ) {
        this.scoreboardMapper = scoreboardMapper;
        this.contestMapper = contestMapper;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }


    public void calculateScoreboardWitchNeed() {
        List<Integer> contestList = scoreboardMapper.getContestNeedCalculate();
        contestList.forEach(contest -> {
            try {
                calculateScoreboardForContest(contest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void calculateScoreboardForContest(int contestId) throws Exception {
        calculateScoreboardForContest(contestId, false);
    }

    public void calculateScoreboardForContest(int contestId, boolean force) throws Exception {
        ContestInfo4Scoreboard contestInfo = buildContestInfo(contestId);
        if (contestInfo == null) {
            throw new Exception("No such contest that contestId = " + contestId);
        }
        int contestStatus = contestMapper.getContestStatus(contestId);
        int contestFrozen = contestMapper.isContestFrozen(contestId);
        List<ContestProblem> problemList = contestMapper.getContestProblem(contestId);
        List<Solution4Scoreboard> submissions = scoreboardMapper.getSubmissions(contestId);
        ScoreboardCalculator scoreboardCalculator;

        switch (contestInfo.getContestType()) {
            case ICPC: {
                scoreboardCalculator = new ScoreboardCalculatorICPC(contestInfo, problemList, submissions);
                break;
            }
            case OI: {
                scoreboardCalculator = new ScoreboardCalculatorOI(contestInfo, problemList, submissions);
                break;
            }
            case IOI: {
                scoreboardCalculator = new ScoreboardCalculatorIOI(contestInfo, problemList, submissions);
                break;
            }
            // new contest type
            default: {
                throw new Exception("ContestType is not Supported, contestType = " + contestInfo.getContestType());
            }
        }

        Scoreboard scoreboard = scoreboardCalculator.calculate(true);
        String scoreboardJson = scoreboard.toJSON(objectMapper);
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.put("ZUCCOJ::SCOREBOARD", Integer.toString(contestId), scoreboardJson);
        if (force || (contestStatus > 0 && contestFrozen == 0)) {
            scoreboardMapper.updateScoreboard(contestId, scoreboardJson);
        }
    }

    private ContestInfo4Scoreboard buildContestInfo(int contestId) {
        Contest contest = contestMapper.getContest(contestId);
        if (contest == null) {
            return null;
        }
        ContestInfo4Scoreboard result = new ContestInfo4Scoreboard(contest, contestMapper.getContestStatus(contestId), contestMapper.isContestFrozen(contestId) == 1);
        return result;
    }

}
