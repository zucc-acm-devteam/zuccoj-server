package top.kealine.zuccoj.structure.scoreboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.kealine.zuccoj.constant.ContestType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard {
    private final long updateTime;
    private final int problemCount;
    private final int contestType;
    private final boolean contestFrozen;
    private final Map<String, ScoreboardEach> scoreboardMap;

    public Scoreboard(long updateTime, int problemCount, int contestType, boolean contestFrozen) {
        this.updateTime = updateTime;
        this.problemCount = problemCount;
        this.contestType = contestType;
        this.contestFrozen = contestFrozen;
        this.scoreboardMap = new HashMap<>();
        this.done = false;
    }

    public void addEachData(ScoreboardEach eachData) {
        this.scoreboardMap.put(eachData.getUsername(), eachData);
    }

    private boolean done;
    private List<ScoreboardEach> scoreboardList;
    public void buildAndSort() {
        this.scoreboardList = new ArrayList<>(this.scoreboardMap.size());
        this.scoreboardList.addAll(this.scoreboardMap.values());

        Comparator<ScoreboardEach> comparator;
        switch (this.contestType) {
            case ContestType.ACM: {
                comparator = (o1, o2) -> {
                    if (o1.getPoint() == o2.getPoint()) {
                        if (o1.getPenalty() == o2.getPenalty()) {
                            return o1.getUsername().compareTo(o2.getUsername());
                        } else {
                            return Long.compare(o1.getPenalty(), o2.getPenalty());
                        }
                    } else {
                        return Integer.compare(o2.getPoint(), o1.getPoint());
                    }
                };
                break;
            }

            case ContestType.IOI:
            case ContestType.OI: {
                comparator = (o1, o2) -> {
                    if (o1.getScore() == o2.getScore()) {
                        return o1.getUsername().compareTo(o2.getUsername());
                    } else {
                        return Long.compare(o2.getScore(), o1.getScore());
                    }
                };
                break;
            }
            default: {
                comparator = Comparator.comparing(ScoreboardEach::getUsername);
                break;
            }
        }
        this.scoreboardList.sort(comparator);

        int rank=0;
        for(ScoreboardEach each: this.scoreboardList) {
            each.setRank(++rank);
        }

        this.done = true;
    }

    public String toJSON(ObjectMapper objectMapper) {
        return toJSON(objectMapper, false);
    }

    public String toJSON(ObjectMapper objectMapper, boolean format) {
        return ScoreboardJsonify.jsonify(this, objectMapper, format);
    }

    public static class ScoreboardEach {
        private int rank;
        private final String username;
        private final String nickname;
        private int point;
        private long penalty;
        private long score;
        private List<ScoreboardCell> cells;

        public String getUsername() {
            return username;
        }

        public String getNickname() {
            return nickname;
        }

        public int getPoint() {
            return point;
        }

        public long getPenalty() {
            return penalty;
        }

        public long getScore() {
            return score;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return rank;
        }

        public List<ScoreboardCell> getCells() {
            return cells;
        }

        public ScoreboardEach(String username, String nickname, List<ScoreboardCell> cells) {
            this.username = username;
            this.nickname = nickname;
            this.point = 0;
            this.penalty = 0;
            this.rank = 0;
            this.score = 0;
            this.cells = cells;
            this.calculatePenalty();
        }

        private void calculatePenalty() {
            for (ScoreboardCell cell: this.cells) {
                if (cell.getRightTime() != null) {
                    this.point++;
                    this.penalty += cell.getWrongTryCount() * 20L;
                    this.penalty += cell.rightTime;
                }
                this.score += cell.getScore();
            }
        }
    }

    public static class ScoreboardCell {
        private final int wrongTryCount;
        private final int pendingTryCont;
        private final Long rightTime;
        private final Long rightTimeInSecond;
        private final int score;
        private final boolean firstBlood;

        public ScoreboardCell() {
            this.wrongTryCount = 0;
            this.pendingTryCont = 0;
            this.rightTime = null;
            this.rightTimeInSecond = null;
            this.score = 0;
            this.firstBlood = false;
        }

        public ScoreboardCell(int wrongTryCount, int pendingTryCont, Long rightTime, Long rightTimeInSecond, int score, boolean firstBlood) {
            this.wrongTryCount = wrongTryCount;
            this.pendingTryCont = pendingTryCont;
            this.rightTime = rightTime;
            this.rightTimeInSecond = rightTimeInSecond;
            this.score = score;
            this.firstBlood = firstBlood;
        }

        public int getWrongTryCount() {
            return wrongTryCount;
        }

        public int getPendingTryCont() {
            return pendingTryCont;
        }

        public Long getRightTime() {
            return rightTime;
        }

        public Long getRightTimeInSecond() {
            return rightTimeInSecond;
        }

        public int getScore() {
            return score;
        }

        public boolean isFirstBlood() {
            return firstBlood;
        }

    }

    public long getUpdateTime() {
        return updateTime;
    }

    public int getProblemCount() {
        return problemCount;
    }

    public int getContestType() {
        return contestType;
    }

    public boolean isContestFrozen() {
        return contestFrozen;
    }

    public boolean isDone() {
        return done;
    }

    public List<ScoreboardEach> getScoreboardList() {
        return scoreboardList;
    }
}
