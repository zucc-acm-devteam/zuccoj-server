package top.kealine.zuccoj.entity;

import java.util.List;

public class JudgeTask {
    private long solutionId;
    private String code;
    private List<Integer> testcaseList;
    private int problemId;
    private int timeLimit;
    private int memoryLimit;
    private int lang;

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Integer> getTestcaseList() {
        return testcaseList;
    }

    public void setTestcaseList(List<Integer> testcaseList) {
        this.testcaseList = testcaseList;
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

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }
}
