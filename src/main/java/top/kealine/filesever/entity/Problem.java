package top.kealine.filesever.entity;

public class Problem {
    private int problemId;
    private String title;

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

    @Override
    public String toString() {
        return "Problem{" +
                "problemId=" + problemId +
                ", title='" + title + '\'' +
                '}';
    }
}
