package top.kealine.zuccoj.util;

import top.kealine.zuccoj.entity.Problem;

public class ProblemUtil {
    public static Problem packageUp(
            int problemId,
            String title,
            String description,
            String input,
            String output,
            String hint,
            int timeLimit,
            int memoryLimit,
            String spj,
            boolean visible,
            String samples,
            String tags
    ) {
        Problem problem = new Problem();
        if (problemId != -1) {
            problem.setProblemId(problemId);
        }
        problem.setTitle(title);
        problem.setDescription(description);
        problem.setInput(input);
        problem.setOutput(output);
        problem.setHint(hint);
        problem.setTimeLimit(timeLimit);
        problem.setMemoryLimit(memoryLimit);
        problem.setSpj(spj != null);
        problem.setVisible(visible);
        problem.setSamples(samples);
        problem.setTags(tags);
        return problem;
    }
}
