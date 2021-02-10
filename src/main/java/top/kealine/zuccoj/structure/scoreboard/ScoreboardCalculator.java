package top.kealine.zuccoj.structure.scoreboard;

import top.kealine.zuccoj.entity.ContestInfo4Scoreboard;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.Solution4Scoreboard;

import java.util.List;

public abstract class ScoreboardCalculator {
    protected ContestInfo4Scoreboard contestInfo;
    protected List<ContestProblem> problemList;
    protected List<Solution4Scoreboard> submissions;

    ScoreboardCalculator(ContestInfo4Scoreboard contestInfo, List<ContestProblem> problemList, List<Solution4Scoreboard> submissions) {
        this.contestInfo = contestInfo;
        this.problemList = problemList;
        this.submissions = submissions;
    }

    public abstract Scoreboard calculate(boolean strict) throws Exception;

    public ContestInfo4Scoreboard getContestInfo() {
        return contestInfo;
    }

    public void setContestInfo(ContestInfo4Scoreboard contestInfo) {
        this.contestInfo = contestInfo;
    }

    public List<ContestProblem> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<ContestProblem> problemList) {
        this.problemList = problemList;
    }

    public List<Solution4Scoreboard> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Solution4Scoreboard> submissions) {
        this.submissions = submissions;
    }
}
