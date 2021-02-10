package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final ScoreboardMapper scoreboardMapper;
    private final ContestMapper contestMapper;

    @Autowired
    ScoreboardRunner(
            ScoreboardMapper scoreboardMapper,
            ContestMapper contestMapper
    ) {
        this.scoreboardMapper = scoreboardMapper;
        this.contestMapper = contestMapper;
    }


    public void calculateScoreboardWitchNeed() {

    }

    public void calculateScoreboardForContest(int contestId) throws Exception {
        ContestInfo4Scoreboard contestInfo = buildContestInfo(contestId);
        if (contestInfo == null) {
            throw new Exception("No such contest that contestId = " + contestId);
        }
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
    }

    private ContestInfo4Scoreboard buildContestInfo(int contestId) {
        Contest contest = contestMapper.getContest(contestId);
        if (contest == null) {
            return null;
        }
        ContestInfo4Scoreboard result = (ContestInfo4Scoreboard) contest;
        result.setContestStatus(contestMapper.getContestStatus(contestId));
        result.setContestFrozen(contestMapper.isContestFrozen(contestId) == 1);
        return result;
    }

}
