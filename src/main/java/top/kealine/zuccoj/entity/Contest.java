package top.kealine.zuccoj.entity;

public class Contest {
    private int contestId;
    private String contestName;
    private String beginTime;
    private String endTime;
    private String freezeTime;
    private String unfreezeTime;
    private boolean isPublic;
    private String password;
    private int contestType;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFreezeTime() {
        return freezeTime;
    }

    public void setFreezeTime(String freezeTime) {
        this.freezeTime = freezeTime;
    }

    public String getUnfreezeTime() {
        return unfreezeTime;
    }

    public void setUnfreezeTime(String unfreezeTime) {
        this.unfreezeTime = unfreezeTime;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getContestType() {
        return contestType;
    }

    public void setContestType(int contestType) {
        this.contestType = contestType;
    }
}
