package top.kealine.fileserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.fileserver.entity.Testcase;
import top.kealine.fileserver.mapper.TestcaseMapper;

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
