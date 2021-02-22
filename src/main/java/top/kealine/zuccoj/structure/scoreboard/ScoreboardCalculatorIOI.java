package top.kealine.zuccoj.structure.scoreboard;

import top.kealine.zuccoj.constant.ContestType;
import top.kealine.zuccoj.constant.JudgeResult;
import top.kealine.zuccoj.entity.ContestInfo4Scoreboard;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.Solution4Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreboardCalculatorIOI extends ScoreboardCalculator
{
    public ScoreboardCalculatorIOI(ContestInfo4Scoreboard contestInfo, List<ContestProblem> problemList, List<Solution4Scoreboard> submissions) {
        super(contestInfo, problemList, submissions);
    }
    private Map<String, ScoreboardCalculatorIOI.IOIScoreboardBuilder> userStatus;
    private Set<Integer> acceptedProblems;
    private Map<String, String> nicknameMap;

    private void initialize() {
        this.userStatus = new HashMap<>();
        this.acceptedProblems = new HashSet<>();
        this.nicknameMap = new HashMap<>();
    }

    private boolean isUserDebut(String username) {
        return !this.userStatus.containsKey(username);
    }

    private void newUser(String username, String nickname) {
        userStatus.put(username, new ScoreboardCalculatorIOI.IOIScoreboardBuilder());
        nicknameMap.put(username, nickname);
    }
    @Override
    public Scoreboard calculate(boolean strict) throws Exception {
        if (strict) {
            if (this.contestInfo.getContestType() != ContestType.IOI) {
                throw new Exception("CONTEST TYPE IS NOT MATCH");
            }
        }

        this.initialize();

        for (Solution4Scoreboard submission: submissions) {
            if (this.isUserDebut(submission.getUsername())) {
                this.newUser(submission.getUsername(), submission.getNickname());
            }

            // ignore SE, CE is valid in IOI
            if (submission.getResult() == JudgeResult.SE) {
                continue;
            }

            ScoreboardCalculatorIOI.IOIScoreboardBuilder userBuilder = userStatus.get(submission.getUsername());

            if (userBuilder.isProblemIsAC(submission.getProblemId())) {
                continue;
            }

            switch (submission.getResult()) {
                case JudgeResult.PD: {
                    userBuilder.pendingSubmission(submission.getProblemId());
                    break;
                }
                case JudgeResult.AC: {
                    userBuilder.submitSubmission(submission.getProblemId(), submission.getPenalty(), !acceptedProblems.contains(submission.getProblemId()), submission.getScore(), submission.getResult());
                    acceptedProblems.add(submission.getProblemId());
                    break;
                }
                default: {
                    userBuilder.submitSubmission(submission.getProblemId(), submission.getPenalty(), false, submission.getScore(), submission.getResult());
                    break;
                }
            }
        }

        return this.buildIOIScoreboard();
    }

    private Scoreboard buildIOIScoreboard() {
        Scoreboard scoreboard = new Scoreboard(System.currentTimeMillis(), this.problemList.size(), this.contestInfo.getContestType(), this.contestInfo.isContestFrozen());

        for (Map.Entry<String, ScoreboardCalculatorIOI.IOIScoreboardBuilder> entry: userStatus.entrySet()) {
            List<Scoreboard.ScoreboardCell> cells = new ArrayList<>(this.problemList.size());
            for (ContestProblem problem: this.problemList) {
                cells.add(entry.getValue().getProblemCell(problem.getProblemId()));
            }
            scoreboard.addEachData(
                    new Scoreboard.ScoreboardEach(
                            entry.getKey(),
                            this.nicknameMap.get(entry.getKey()),
                            cells
                    )
            );
        }

        scoreboard.buildAndSort();

        return scoreboard;
    }

    private static class IOIScoreboardBuilder {
        Map<Integer, ScoreboardCalculatorIOI.IOIProblemStatus> problemStatus;

        IOIScoreboardBuilder() {
            this.problemStatus = new HashMap<>();
        }

        boolean isProblemIsAC(int problemId) {
            return problemStatus.containsKey(problemId) && problemStatus.get(problemId).isAccepted();
        }

        void pendingSubmission(int problemId) {
            if (!problemStatus.containsKey(problemId)) {
                problemStatus.put(problemId, new ScoreboardCalculatorIOI.IOIProblemStatus());
            }
            problemStatus.get(problemId).pending();
        }
        void submitSubmission(int problemId, long time, boolean firstBlood, int score, int result) {
            if (!problemStatus.containsKey(problemId)) {
                problemStatus.put(problemId, new ScoreboardCalculatorIOI.IOIProblemStatus());
            }
            problemStatus.get(problemId).submit(time, firstBlood, score, result);
        }

        public Scoreboard.ScoreboardCell getProblemCell(int problemId) {
            ScoreboardCalculatorIOI.IOIProblemStatus status = this.problemStatus.getOrDefault(problemId, null);
            if (status == null) {
                return new Scoreboard.ScoreboardCell();
            }
            return status.toScoreboardCell();
        }
    }


    private static class IOIProblemStatus {
        private int score;
        private int wrongTryCount;
        private int pendingTryCont;
        private Long rightTimeInSecond;
        private boolean firstBlood;

        IOIProblemStatus() {
            this.score = 0;
            this.wrongTryCount = 0;
            this.pendingTryCont = 0;
            this.rightTimeInSecond = null;
            this.firstBlood = false;
        }

        public Scoreboard.ScoreboardCell toScoreboardCell() {
            return new Scoreboard.ScoreboardCell(
                    this.wrongTryCount,
                    this.pendingTryCont,
                    rightTimeInSecond == null ? null : (long) Math.ceil(this.rightTimeInSecond/60.0),
                    this.rightTimeInSecond,
                    score,
                    this.firstBlood
            );
        }

        public int getWrongTryCount() {
            return wrongTryCount;
        }

        public int getPendingTryCont() {
            return pendingTryCont;
        }

        public Long getRightTimeInSecond() {
            return rightTimeInSecond;
        }

        public boolean isFirstBlood() {
            return firstBlood;
        }

        public boolean isAccepted() {
            return rightTimeInSecond != null;
        }

        public int getScore() {
            return score;
        }

        public void pending() {
            this.pendingTryCont++;
        }

        public void submit(long time, boolean firstBlood, int score, int result) {
            if (result == JudgeResult.AC) {
                this.rightTimeInSecond = time;
                this.firstBlood = firstBlood;
            } else {
                this.wrongTryCount++;
                this.rightTimeInSecond = null;
            }
            this.score = Math.max(this.score, score);
        }
    }
}
