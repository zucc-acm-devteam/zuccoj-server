package top.kealine.zuccoj.entity;

public class Solution {
    private long solutionId;
    private int problemId;
    private String username;
    private String code;
    private String submitTime;
    private int result;
    private int memoryUsed;
    private int timeUsed;
    private int codeLength;
    private int lang;
    private String remark;

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
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

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}