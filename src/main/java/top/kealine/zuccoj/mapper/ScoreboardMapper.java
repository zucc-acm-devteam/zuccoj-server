package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.Solution4Scoreboard;

import java.util.List;

@Mapper
public interface ScoreboardMapper {

    @Select("SELECT\n" +
            "    ta.contest_id contestId,\n" +
            "    solution_id solutionId,\n" +
            "    problem_id problemId,\n" +
            "    username,\n" +
            "    submit_time submitTime,\n" +
            "    result,\n" +
            "    score,\n" +
            "    (SELECT nickname FROM users WHERE users.username=tb.username) nickname,\n" +
            "    (UNIX_TIMESTAMP(submit_time) - UNIX_TIMESTAMP(begin_time)) penalty,\n" +
            "    IF (freeze_time IS NULL, 0, IF(submit_time >= freeze_time, IF(unfreeze_time IS NULL, 1, IF(submit_time<=unfreeze_time,1,0)), 0)) freeze\n" +
            "FROM (SELECT * FROM contest WHERE contest_id = #{contestId}) ta \n" +
            "JOIN (SELECT * FROM solutions WHERE contest_id = #{contestId}) tb \n" +
            "ON ta.contest_id = tb.contest_id WHERE submit_time >= begin_time AND submit_time <= end_time \n" +
            "ORDER BY submit_time ASC")
    List<Solution4Scoreboard> getSubmissions(int contestId);

    @Insert("INSERT INTO scoreboard(contest_id) VALUES(#{contestId})")
    void newContest(int contestId);

    @Update("UPDATE scoreboard SET scoreboard_json=#{scoreboardJson} WHERE contest_id=#{contestId}")
    void updateScoreboard(int contestId, String scoreboardJson);

    @Select("SELECT contest.contest_id contestId FROM contest JOIN scoreboard ON contest.contest_id=scoreboard.contest_id WHERE (NOW() >= begin_time AND NOW() <= end_time) OR (scoreboard_json is NULL)")
    List<Integer> getContestNeedCalculate();
}
