package top.kealine.zuccoj.structure.scoreboard;

import top.kealine.zuccoj.entity.ContestInfo4Scoreboard;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.Solution4Scoreboard;

import java.util.List;

public class ScoreboardCalculatorIOI extends ScoreboardCalculator
{
    public ScoreboardCalculatorIOI(ContestInfo4Scoreboard contestInfo, List<ContestProblem> problemList, List<Solution4Scoreboard> submissions) {
        super(contestInfo, problemList, submissions);
    }

    @Override
    public Scoreboard calculate(boolean strict) throws Exception {
        return null;
    }
}
