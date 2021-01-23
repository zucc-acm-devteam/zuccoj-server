package top.kealine.zuccoj.service;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kealine.zuccoj.config.OnlineJudgeConfig;
import top.kealine.zuccoj.entity.Testcase;
import top.kealine.zuccoj.mapper.TestcaseMapper;
import top.kealine.zuccoj.util.ServerFileUtil;
import top.kealine.zuccoj.util.ZipUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TestcaseService {
    private final TestcaseMapper testcaseMapper;
    private final OnlineJudgeConfig onlineJudgeConfig;

    @Autowired
    TestcaseService(TestcaseMapper testcaseMapper, OnlineJudgeConfig onlineJudgeConfig) {
        this.testcaseMapper = testcaseMapper;
        this.onlineJudgeConfig = onlineJudgeConfig;
    }

    public List<Testcase> getTestcaseByProblemId(int problemId) {
        return testcaseMapper.getTestcaseByProblemId(problemId);
    }

    public Testcase getTestcase(int testcaseId) {
        return testcaseMapper.getTestcase(testcaseId);
    }

    public String getTestcasePath(int problemId, int testcaseId, boolean input) {
        return onlineJudgeConfig.dataDir + problemId + onlineJudgeConfig.separator + testcaseId + (input?".in":".ans");
    }

    public String getTestcasePath(int testcaseId, boolean input) {
        return onlineJudgeConfig.dataDir + getProblemIdByTestcaseId(testcaseId) + onlineJudgeConfig.separator + testcaseId + (input?".in":".ans");
    }

    public boolean hasTestcase(int testcaseId) {
        return testcaseMapper.countTestcaseById(testcaseId)>0;
    }

    public int getProblemIdByTestcaseId(int testcaseId) {
        return testcaseMapper.getProblemIdByTestcaseId(testcaseId);
    }

    public int newTestcase(int problemId, MultipartFile input, MultipartFile output) {
        int testcaseId = -1;
        try {
            String inputName = input.getOriginalFilename();
            String outputName = output.getOriginalFilename();
            String inputMD5 = ServerFileUtil.md5Hex(input);
            String outputMD5 = ServerFileUtil.md5Hex(output);
            long inputSize = input.getSize();
            long outputSize = output.getSize();

            Testcase testcase = new Testcase();
            testcase.setProblemId(problemId);
            testcase.setInputFilename(inputName);
            testcase.setOutputFilename(outputName);
            testcase.setInputMD5(inputMD5);
            testcase.setOutputMD5(outputMD5);
            testcase.setInputSize(inputSize);
            testcase.setOutputSize(outputSize);
            testcaseMapper.newTestcase(testcase);
            testcaseId = testcase.getTestcaseId();

            ServerFileUtil.save(input, getTestcasePath(problemId, testcaseId, true));
            ServerFileUtil.save(output, getTestcasePath(problemId, testcaseId, false));

            return testcase.getTestcaseId();
        } catch (Exception e) {
            e.printStackTrace();
            testcaseMapper.delTestcase(testcaseId);
            return -1;
        }
    }

    public List<String> newTestcaseByZip(int problemId, MultipartFile zip) {
        String name = UUID.randomUUID().toString() + ".zip";
        String path = onlineJudgeConfig.tempDir + name;
        try {
            ServerFileUtil.save(zip, path);
            Map<String,String> unzipResult = ZipUtil.unzipTestcasePackage(path);
            String unzipDir = ServerFileUtil.getFilenameWithoutExtend(path) + onlineJudgeConfig.separator;
            ImmutableList.Builder<String> result = ImmutableList.builder();
            for(Map.Entry<String, String> testcase: unzipResult.entrySet()) {
                String inputFile = unzipDir + testcase.getKey() + ".in";
                String outputFile = unzipDir + testcase.getKey() + ".ans";
                int testcaseId = newTestcase(problemId, ServerFileUtil.fileToMultipartFile(inputFile), ServerFileUtil.fileToMultipartFile(outputFile));
                result.add(String.format("testcaseId = %s, inputFile = %s, outputFile = %s", testcaseId, testcase.getKey()+".in", testcase.getKey()+testcase.getValue()));
            }
            return result.build();
        } catch (Exception e) {
            e.printStackTrace();
            return ImmutableList.of();
        }
    }

    public boolean delTestcase(int testcaseId) {
        try {
            int problemId = getProblemIdByTestcaseId(testcaseId);
            ServerFileUtil.delete(getTestcasePath(problemId, testcaseId, true));
            ServerFileUtil.delete(getTestcasePath(problemId, testcaseId, false));
            testcaseMapper.delTestcase(testcaseId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
