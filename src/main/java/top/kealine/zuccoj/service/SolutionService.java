package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.mapper.SolutionMapper;

@Service
public class SolutionService {
    private SolutionMapper solutionMapper;

    @Autowired
    SolutionService(SolutionMapper solutionMapper) {
        this.solutionMapper = solutionMapper;
    }

}
