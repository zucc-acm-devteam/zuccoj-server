package top.kealine.zuccoj.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class ContestProblem {
    private int contestId;
    private int problemOrder;
    private int problemId;

    public ContestProblem() {}

    public ContestProblem(int contestId, int problemOrder, int problemId) {
        this.contestId = contestId;
        this.problemOrder = problemOrder;
        this.problemId = problemId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getProblemOrder() {
        return problemOrder;
    }

    public void setProblemOrder(int problemOrder) {
        this.problemOrder = problemOrder;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public static List<ContestProblem> buildContestProblemList(int contestId, String json) throws JsonProcessingException {
        List<Integer> problems = new ObjectMapper().readValue(json, new TypeReference<List<Integer>>() {});
        ImmutableList.Builder<ContestProblem> builder = new ImmutableList.Builder<>();
        // order from 0
        for(int i=0;i<problems.size();i++) {
            builder.add(new ContestProblem(contestId, i, problems.get(i)));
        }
        return builder.build();
    }
}
