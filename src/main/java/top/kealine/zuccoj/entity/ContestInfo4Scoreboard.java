package top.kealine.zuccoj.entity;

public class ContestInfo4Scoreboard extends Contest {
    private int contestStatus;
    private boolean contestFrozen;

    public ContestInfo4Scoreboard(int contestId, String contestName, String beginTime, String endTime, String freezeTime, String unfreezeTime, boolean isPublic, String password, int contestType) {
        super(contestId, contestName, beginTime, endTime, freezeTime, unfreezeTime, isPublic, password, contestType);
    }

    public ContestInfo4Scoreboard(Contest contest, int contestStatus, boolean contestFrozen) {
        this(contest.getContestId(), contest.getContestName(), contest.getBeginTime(), contest.getEndTime(), contest.getFreezeTime(), contest.getUnfreezeTime(), contest.isPublic(), contest.getPassword(), contest.getContestType());
        this.contestStatus = contestStatus;
        this.contestFrozen = contestFrozen;
    }

    public int getContestStatus() {
        return contestStatus;
    }

    public void setContestStatus(int contestStatus) {
        this.contestStatus = contestStatus;
    }

    public boolean isContestFrozen() {
        return contestFrozen;
    }

    public void setContestFrozen(boolean contestFrozen) {
        this.contestFrozen = contestFrozen;
    }
}
