package top.kealine.zuccoj.util.scoreboard;

import top.kealine.zuccoj.entity.ContestInfo4Scoreboard;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.Solution4Scoreboard;

import java.util.List;

public class ScoreboardCalculatorOI extends ScoreboardCalculator{
    public ScoreboardCalculatorOI(ContestInfo4Scoreboard contestInfo, List<ContestProblem> problemList, List<Solution4Scoreboard> submissions) {
        super(contestInfo, problemList, submissions);
    }
}
