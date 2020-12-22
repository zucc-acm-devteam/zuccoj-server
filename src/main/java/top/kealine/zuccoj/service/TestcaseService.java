package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.entity.Testcase;
import top.kealine.zuccoj.mapper.TestcaseMapper;

import java.util.List;

@Service
public class TestcaseService {
    private final TestcaseMapper testcaseMapper;

    @Autowired
    TestcaseService(TestcaseMapper testcaseMapper) {
        this.testcaseMapper = testcaseMapper;
    }

    public List<Testcase> getTestcaseByProblemId(int problemId) {
        return testcaseMapper.getTestcaseByProblemId(problemId);
    }
}
