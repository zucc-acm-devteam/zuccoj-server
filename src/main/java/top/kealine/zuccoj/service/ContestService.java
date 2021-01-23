package top.kealine.zuccoj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kealine.zuccoj.entity.Contest;
import top.kealine.zuccoj.entity.ContestProblem;
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
        for (int i=oldContestProblemCount+1;i<newContestProblemCount;i++) {
            contestMapper.insertContestProblem(contestProblems.get(i));
        }
    }
}
