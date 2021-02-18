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

public class ScoreboardCalculatorICPC extends ScoreboardCalculator{

    public ScoreboardCalculatorICPC(ContestInfo4Scoreboard contestInfo, List<ContestProblem> problemList, List<Solution4Scoreboard> submissions) {
        super(contestInfo, problemList, submissions);
    }
    private Map<String, ICPCScoreboardBuilder> userStatus;
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
        userStatus.put(username, new ICPCScoreboardBuilder());
        nicknameMap.put(username, nickname);
    }

    @Override
    public Scoreboard calculate(boolean strict) throws Exception{
        if (strict) {
            if (this.contestInfo.getContestType() != ContestType.ICPC) {
                throw new Exception("CONTEST TYPE IS NOT MATCH");
            }
        }

        this.initialize();

        for(Solution4Scoreboard submission: submissions) {
            if (this.isUserDebut(submission.getUsername())) {
                this.newUser(submission.getUsername(), submission.getNickname());
            }

            // ignore CE/SE
            if (JudgeResult.isResultIgnorable(submission.getResult())) {
                continue;
            }

            ICPCScoreboardBuilder userBuilder = userStatus.get(submission.getUsername());

            // ignore submissions after AC
            if (userBuilder.isProblemIsAC(submission.getProblemId())) {
                continue;
            }

            if (this.contestInfo.isContestFrozen() && submission.isFrozen()) {
                userBuilder.pendingSubmission(submission.getProblemId());
                continue;
            }

            switch (submission.getResult()) {
                case JudgeResult.PD: {
                    userBuilder.pendingSubmission(submission.getProblemId());
                    break;
                }
                case JudgeResult.AC: {
                    userBuilder.acceptedSubmission(submission.getProblemId(), submission.getPenalty(), !this.acceptedProblems.contains(submission.getProblemId()));
                    acceptedProblems.add(submission.getProblemId());
                    break;
                }
                default: {
                    userBuilder.wrongSubmission(submission.getProblemId());
                    break;
                }
            }

        }

        return this.buildICPCScoreboard();
    }

    private Scoreboard buildICPCScoreboard() {
        Scoreboard scoreboard = new Scoreboard(System.currentTimeMillis(), this.problemList.size(), this.contestInfo.getContestType(), this.contestInfo.isContestFrozen());

        for (Map.Entry<String, ICPCScoreboardBuilder> entry: userStatus.entrySet()) {
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

    private static class ICPCScoreboardBuilder {
        Map<Integer, ICPCProblemStatus> problemStatus;

        ICPCScoreboardBuilder() {
            this.problemStatus = new HashMap<>();
        }

        boolean isProblemIsAC(int problemId) {
            return problemStatus.containsKey(problemId) && problemStatus.get(problemId).isAccepted();
        }

        void pendingSubmission(int problemId) {
            if (!problemStatus.containsKey(problemId)) {
                problemStatus.put(problemId, new ICPCProblemStatus());
            }
            problemStatus.get(problemId).pending();
        }

        void acceptedSubmission(int problemId, long time, boolean firstBlood) {
            if (!problemStatus.containsKey(problemId)) {
                problemStatus.put(problemId, new ICPCProblemStatus());
            }
            problemStatus.get(problemId).accepted(time, firstBlood);
        }

        void wrongSubmission(int problemId) {
            if (!problemStatus.containsKey(problemId)) {
                problemStatus.put(problemId, new ICPCProblemStatus());
            }
            problemStatus.get(problemId).wrong();
        }

        public Scoreboard.ScoreboardCell getProblemCell(int problemId) {
            ICPCProblemStatus status = this.problemStatus.getOrDefault(problemId, null);
            if (status == null) {
                return new Scoreboard.ScoreboardCell();
            }
            return status.toScoreboardCell();
        }
    }

    private static class ICPCProblemStatus {
        private int wrongTryCount;
        private int pendingTryCont;
        private Long rightTimeInSecond;
        private boolean firstBlood;

        ICPCProblemStatus() {
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
                    0,
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

        public void pending() {
            this.pendingTryCont++;
        }

        public void accepted(long time, boolean firstBlood) {
            this.rightTimeInSecond = time;
            this.firstBlood = firstBlood;
        }

        public void wrong() {
            this.wrongTryCount++;
        }
    }
}
