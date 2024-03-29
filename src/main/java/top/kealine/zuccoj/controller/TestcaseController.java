package top.kealine.zuccoj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.Testcase;
import top.kealine.zuccoj.service.TestcaseService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;
import top.kealine.zuccoj.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testcase")
public class TestcaseController {
    private final UserService userService;
    private final TestcaseService testcaseService;

    @Autowired
    TestcaseController(UserService userService, TestcaseService testcaseService) {
        this.userService = userService;
        this.testcaseService = testcaseService;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> get(
            @RequestParam(name = "problemId", required = true) int problemId,
            HttpServletRequest request) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.baseData(testcaseService.getTestcaseByProblemId(problemId));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(
            @RequestParam(name = "problemId", required = true) int problemId,
            @RequestParam(name = "inputFile", required = true) MultipartFile inputFile,
            @RequestParam(name = "outputFile", required = true) MultipartFile outputFile,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        int testcaseId = testcaseService.newTestcase(problemId, inputFile, outputFile);
        if (testcaseId < 0) {
            return ResponseConstant.X_UPLOAD_FAILED;
        } else {
            return ResponseConstant.V_UPLOAD_SUCCESS;
        }
    }

    @RequestMapping(value = "/uploadZip", method = RequestMethod.POST)
    public Map<String, Object> uploadZip(
            @RequestParam(name = "problemId", required = true) int problemId,
            @RequestParam(name = "zip", required = true) MultipartFile zip,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        List<String> result = testcaseService.newTestcaseByZip(problemId, zip);
        return BaseResponsePackageUtil.baseData(result);
    }

    @RequestMapping(value = "/delOne", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE})
    public Map<String, Object> delOne(
            @RequestParam(name = "testcaseId", required = true) int testcaseId,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        if (!testcaseService.hasTestcase(testcaseId)) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (testcaseService.delTestcase(testcaseId)) {
            return ResponseConstant.V_DELETE_SUCCESS;
        } else {
            return ResponseConstant.X_DELETE_FAILED;
        }
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> downloadOne(
            @RequestParam(name = "id", required = true) int testcaseId,
            @RequestParam(name = "input", required = true) boolean isInput,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.DATA_VIEWER)) {
            return ResponseEntity.status(403).build();
        }
        Testcase testcase = testcaseService.getTestcase(testcaseId);
        if (testcase == null) {
            return ResponseEntity.notFound().build();
        }
        File file = new File(testcaseService.getTestcasePath(testcaseId, isInput));
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = HttpUtil.fileHeaders(testcaseId + (isInput?".in":".ans"));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }

    @RequestMapping(value = "/score", method = RequestMethod.POST)
    public Map<String, Object> updateTestcaseScore(
            @RequestParam(name = "testcaseId", required = true) int testcaseId,
            @RequestParam(name = "score", required = true) int score,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        if (!testcaseService.hasTestcase(testcaseId)) {
            return ResponseConstant.X_NOT_FOUND;
        }
        testcaseService.updateTestcaseScore(testcaseId, score);
        return ResponseConstant.V_UPDATE_SUCCESS;
    }

}
