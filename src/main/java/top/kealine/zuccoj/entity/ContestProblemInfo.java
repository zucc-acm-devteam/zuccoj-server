package top.kealine.zuccoj.entity;

public class ContestProblemInfo {
    private int contestId;
    private int problemId;
    private int realProblemId;
    private String title;
    private int timeLimit;
    private int memoryLimit;
    private int submitted;
    private int solved;
    private int status;

    // hide problemId if contest is not finish
    public void hideProblemId() {
        this.realProblemId = this.problemId;
    }

    public void hideProblemInfoInOI() {
        this.status = 0;
        this.submitted = -1;
        this.solved = -1;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getRealProblemId() {
        return realProblemId;
    }

    public void setRealProblemId(int realProblemId) {
        this.realProblemId = realProblemId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
