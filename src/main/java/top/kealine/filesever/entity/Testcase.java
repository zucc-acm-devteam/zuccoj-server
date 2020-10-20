package top.kealine.filesever.entity;

public class Testcase {
    private int testcaseId;
    private int problemId;
    private String inputFilename;
    private String outputFilename;
    private String inputMD5;
    private String outputMD5;

    public int getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(int testcaseId) {
        this.testcaseId = testcaseId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getInputFilename() {
        return inputFilename;
    }

    public void setInputFilename(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public String getInputMD5() {
        return inputMD5;
    }

    public void setInputMD5(String inputMD5) {
        this.inputMD5 = inputMD5;
    }

    public String getOutputMD5() {
        return outputMD5;
    }

    public void setOutputMD5(String outputMD5) {
        this.outputMD5 = outputMD5;
    }
}
