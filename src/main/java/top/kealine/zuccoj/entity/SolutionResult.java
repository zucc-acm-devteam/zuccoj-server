package top.kealine.zuccoj.entity;

import java.util.List;

public class SolutionResult {
    private long solutionId;
    private int result;
    private int memoryUsed;
    private int timeUsed;
    private String remark;
    private String judgehost;
    private List<Integer> passTestcase;
    private int score;

    public SolutionResult() {}
    public SolutionResult(long solutionId, int result, int memoryUsed, int timeUsed, String remark, String judgehost, List<Integer> passTestcase) {
        this.solutionId = solutionId;
        this.result = result;
        this.memoryUsed = memoryUsed;
        this.timeUsed = timeUsed;
        this.remark = remark;
        this.judgehost = judgehost;
        this.passTestcase = passTestcase;
    }

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(int memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getJudgehost() {
        return judgehost;
    }

    public void setJudgehost(String judgehost) {
        this.judgehost = judgehost;
    }

    public List<Integer> getPassTestcase() {
        return passTestcase;
    }

    public void setPassTestcase(List<Integer> passTestcase) {
        this.passTestcase = passTestcase;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
