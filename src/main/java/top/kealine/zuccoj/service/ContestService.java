package top.kealine.zuccoj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kealine.zuccoj.entity.Contest;
import top.kealine.zuccoj.entity.ContestInfo;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.ContestProblemInfo;
import top.kealine.zuccoj.entity.UserNickname;
import top.kealine.zuccoj.mapper.ContestMapper;

import java.util.List;

@Service
public class ContestService {
    private final ContestMapper contestMapper;

    @Autowired
    ContestService(ContestMapper contestMapper) {
        this.contestMapper = contestMapper;
    }

    public Contest getContest(int contestId) {
        return contestMapper.getContest(contestId);
    }

    public List<ContestProblem> getContestProblem(int contestId) {
        return contestMapper.getContestProblem(contestId);
    }

    public ContestProblem getContestProblem(int contestId, int problemOrder) {
        return contestMapper.getContestProblemByOrder(contestId, problemOrder);
    }

    public int getContestCount() {
        return contestMapper.getContestCount();
    }

    @Transactional
    public int newContest(Contest contest, String problemListJson) throws JsonProcessingException {
        contestMapper.insertContest(contest);
        int contestId = contest.getContestId();
        List<ContestProblem> contestProblems = ContestProblem.buildContestProblemList(contestId, problemListJson);
        contestProblems.forEach(contestMapper::insertContestProblem);
        return contestId;
    }

    @Transactional
    public void updateContest(Contest contest, String problemListJson) throws JsonProcessingException {
        contestMapper.updateContest(contest);
        int contestId = contest.getContestId();
        int oldContestProblemCount = contestMapper.getContestProblemCount(contestId);
        List<ContestProblem> contestProblems = ContestProblem.buildContestProblemList(contestId, problemListJson);
        int newContestProblemCount = contestProblems.size();
        if (oldContestProblemCount > newContestProblemCount) {
            contestMapper.deleteContestProblemBiggerThanOrder(contestId, newContestProblemCount - 1); // order from 0
            oldContestProblemCount = newContestProblemCount;
        }
        for (int i=0;i<oldContestProblemCount;i++) {
            contestMapper.updateContestProblem(contestProblems.get(i));
        }
        for (int i=oldContestProblemCount;i<newContestProblemCount;i++) {
            contestMapper.insertContestProblem(contestProblems.get(i));
        }
    }

    public ContestInfo getContestInfo(String username, int contestId) {
        return contestMapper.getContestInfo(username==null?"":username, contestId);
    }

    public List<ContestInfo> getContestInfoList(String username, int page, int pageSize) {
        return contestMapper.getContestInfoList(username==null?"":username, (page-1)*pageSize, pageSize);
    }

    public List<UserNickname> getContestMember(int contestId, boolean isPublic) {
        if (isPublic) {
            return contestMapper.getPublicContestMember(contestId);
        } else {
            return contestMapper.getPrivateContestMember(contestId);
        }
    }

    public void newContestMember(int contestId, String username) {
        contestMapper.newContestMember(contestId, username);
    }

    public boolean isMemberOfContest(int contestId, String username) {
        return contestMapper.checkMemberOfContest(contestId, username) > 0;
    }

    public Integer getContestStatus(int contestId) {
        return contestMapper.getContestStatus(contestId);
    }

    public List<ContestProblemInfo> getContestProblemInfoList(int contestId, String username) {
        return contestMapper.getContestProblemInfoList(contestId, username);
    }
}
