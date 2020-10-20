package top.kealine.filesever.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.filesever.entity.Problem;
import top.kealine.filesever.mapper.ProblemMapper;

import java.util.List;

@Service
public class ProblemService {

    private final ProblemMapper problemMapper;

    @Autowired
    ProblemService(ProblemMapper problemMapper) {
        this.problemMapper = problemMapper;
    }

    public int getProblemsCount() {
        return problemMapper.getProblemCount();
    }

    public List<Problem> getProblemsByPaging(int page, int pageSize) {
        return problemMapper.getProblemByPaging((page-1)*pageSize, pageSize);
    }
}
