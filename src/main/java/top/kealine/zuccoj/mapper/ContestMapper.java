package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.Contest;
import top.kealine.zuccoj.entity.ContestInfo;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.UserNickname;

import java.util.List;

@Mapper
public interface ContestMapper {
    @Insert("INSERT INTO contest(contest_name, begin_time, end_time, freeze_time, unfreeze_time, is_public, password, contest_type) \n" +
            "VALUES(#{contestName}, #{beginTime}, #{endTime}, #{freezeTime}, #{unfreezeTime}, #{isPublic}, #{password}, #{contestType})")
    @Options(useGeneratedKeys = true, keyProperty = "contestId")
    void insertContest(Contest contest);

    @Update("UPDATE contest SET contest_name=#{contestName}, begin_time=#{beginTime}, end_time=#{endTime}, freeze_time=#{freezeTime}, " +
            "unfreeze_time=#{unfreezeTime}, is_public=#{isPublic}, password=#{password}, contest_type=#{contestType} WHERE contest_id=#{contestId}")
    void updateContest(Contest contest);

    @Insert("INSERT INTO contest_problem(contest_id, problem_order, problem_id) VALUES(#{contestId}, #{problemOrder}, #{problemId})")
    void insertContestProblem(ContestProblem contestProblem);

    /// ! bigger than problemOrder
    @Delete("DELETE FROM contest_problem WHERE contest_id=#{contestId} AND problem_order>#{problemOrder}")
    void deleteContestProblemBiggerThanOrder(int contestId, int problemOrder);

    @Update("UPDATE contest_problem SET problem_id=#{problemId} WHERE contest_id=#{contestId} AND problem_order=#{problemOrder}")
    void updateContestProblem(ContestProblem contestProblem);

    @Select("SELECT COUNT(*) FROM contest_problem WHERE contest_id=#{contestId}")
    int getContestProblemCount(int contestId);

    @Select("SELECT COUNT(*) FROM contest")
    int getContestCount();

    @Select("SELECT contest_id contestId, contest_name contestName, begin_time beginTime, end_time endTime, freeze_time freezeTime, " +
            "unfreeze_time unfreezeTime, is_public isPublic, password, contest_type contestType FROM contest WHERE contest_id=#{contestId}")
    Contest getContest(int contestId);

    @Select("SELECT contest_id contestId, problem_order problemOrder, problem_id problemId FROM contest_problem WHERE contest_id=#{contestId} \n" +
            "ORDER BY problem_order ASC")
    List<ContestProblem> getContestProblem(int contestId);

    @Select("SELECT\n" +
            "  contest_id contestId,\n" +
            "  contest_name contestName,\n" +
            "  begin_time beginTime,\n" +
            "  end_time endTime,\n" +
            "  is_public isPublic,\n" +
            "  contest_type contestType,\n" +
            "  (IF(\n" +
            "    is_public=1, (1), \n" +
            "    (IF((SELECT COUNT(*) FROM contest_member WHERE contest_member.contest_id=contest.contest_id AND contest_member.username=#{username}) > 0, 1, 0))\n" +
            "  )) `status`,\n" +
            "  (SELECT COUNT(*) FROM contest_problem WHERE contest_problem.contest_id = contest.contest_id) problemCount,\n" +
            "  (IF(\n" +
            "    is_public=1, \n" +
            "    (SELECT count FROM contest_submitter WHERE contest_submitter.contest_id = contest.contest_id), \n" +
            "    (SELECT COUNT(*) FROM contest_member WHERE contest_member.contest_id = contest.contest_id)\n" +
            "  )) memberCount\n" +
            "FROM\n" +
            "  contest\n" +
            "ORDER BY contest_id DESC\n" +
            "LIMIT #{offset}, #{size}")
    List<ContestInfo> getContestInfoList(String username, int offset, int size);

    @Select("SELECT\n" +
            "  contest_id contestId,\n" +
            "  contest_name contestName,\n" +
            "  begin_time beginTime,\n" +
            "  end_time endTime,\n" +
            "  is_public isPublic,\n" +
            "  contest_type contestType,\n" +
            "  (IF(\n" +
            "    is_public=1, (1), \n" +
            "    (IF((SELECT COUNT(*) FROM contest_member WHERE contest_member.contest_id=contest.contest_id AND contest_member.username=#{username}) > 0, 1, 0))\n" +
            "  )) `status`,\n" +
            "  (SELECT COUNT(*) FROM contest_problem WHERE contest_problem.contest_id = contest.contest_id) problemCount,\n" +
            "  (IF(\n" +
            "    is_public=1, \n" +
            "    (SELECT count FROM contest_submitter WHERE contest_submitter.contest_id = contest.contest_id), \n" +
            "    (SELECT COUNT(*) FROM contest_member WHERE contest_member.contest_id = contest.contest_id)\n" +
            "  )) memberCount\n" +
            "FROM\n" +
            "  contest\n" +
            "WHERE\n" +
            "  contest_id = #{contestId}")
    ContestInfo getContestInfo(String username, int contestId);

    @Select("SELECT t.username username, nickname FROM (SELECT DISTINCT username FROM solutions WHERE contest_id=#{contestId}) t JOIN users ON t.username = users.username")
    List<UserNickname> getPublicContestMember(int contestId);

    @Select("SELECT t.username username, nickname FROM (SELECT username FROM contest_member WHERE contest_id=#{contestId}) t JOIN users ON t.username = users.username")
    List<UserNickname> getPrivateContestMember(int contestId);

    @Insert("INSERT INTO contest_member(contest_id, username) VALUES(#{contestId}, #{username})")
    void newContestMember(int contestId, String username);
}
