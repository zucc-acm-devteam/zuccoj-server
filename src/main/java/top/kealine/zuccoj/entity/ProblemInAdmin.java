package top.kealine.zuccoj.entity;

public class ProblemInAdmin {
    private int problemId;
    private String title;
    private int timeLimit;
    private int memoryLimit;
    private int testcaseCnt;
    private boolean visible;

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

    public int getTestcaseCnt() {
        return testcaseCnt;
    }

    public void setTestcaseCnt(int testcaseCnt) {
        this.testcaseCnt = testcaseCnt;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
