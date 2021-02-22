package top.kealine.zuccoj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.Judgehost;
import top.kealine.zuccoj.entity.SolutionResult;
import top.kealine.zuccoj.entity.Testcase;
import top.kealine.zuccoj.service.JudgehostService;
import top.kealine.zuccoj.service.SolutionService;
import top.kealine.zuccoj.service.TestcaseService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;
import top.kealine.zuccoj.util.HttpUtil;
import top.kealine.zuccoj.util.IpUtil;
import top.kealine.zuccoj.util.PasswordUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/judgehost")
public class JudgehostController {
    private final JudgehostService judgehostService;
    private final UserService userService;
    private final TestcaseService testcaseService;
    private final SolutionService solutionService;
    private final ObjectMapper objectMapper;

    @Autowired
    JudgehostController(
            JudgehostService judgehostService,
            UserService userService,
            TestcaseService testcaseService,
            SolutionService solutionService,
            ObjectMapper objectMapper
    ) {
        this.judgehostService = judgehostService;
        this.userService = userService;
        this.testcaseService = testcaseService;
        this.solutionService = solutionService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Map<String, Object> newJudgehost(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Judgehost judgehost = judgehostService.getJudgehostByName(username);
        if (judgehost!=null) {
            return ResponseConstant.X_USER_ALREADY_EXISTS;
        }
        judgehostService.newJudgehost(username, password);
        judgehostService.log(username, IpUtil.getIpAddr(request), String.format("New judgehost by %s",userService.getUserFromSession(request.getSession()).getUsername()));
        return ResponseConstant.V_ADD_SUCCESS;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.DELETE})
    public Map<String, Object> deleteJudgehost(
            @RequestParam(name = "username", required = true) String username,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Judgehost judgehost = judgehostService.getJudgehostByName(username);
        if (judgehost == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        judgehostService.deleteJudgehost(username);
        judgehostService.log(username, IpUtil.getIpAddr(request), String.format("Delete judgehost by %s",userService.getUserFromSession(request.getSession()).getUsername()));
        return ResponseConstant.V_DELETE_SUCCESS;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateJudgehost(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Judgehost judgehost = judgehostService.getJudgehostByName(username);
        if (judgehost == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        judgehostService.updateJudgehost(username, password);
        judgehostService.log(username, IpUtil.getIpAddr(request), String.format("Update judgehost by %s",userService.getUserFromSession(request.getSession()).getUsername()));
        return ResponseConstant.V_UPDATE_SUCCESS;
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Map<String, Object> judgehostStatus(
            HttpServletRequest request
    ) {
//        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
//            return ResponseConstant.X_ACCESS_DENIED;
//        }
        return BaseResponsePackageUtil.baseData(judgehostService.getJudgehostStatus());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password,
            HttpServletRequest request
    ) {
        Judgehost judgehost = judgehostService.getJudgehostByName(username);
        if (judgehost == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        String ip = IpUtil.getIpAddr(request);
        if (judgehostService.checkJudgehostPassword(judgehost, password)) {
            String token = PasswordUtil.generateToken();
            judgehostService.assignTokenToJudgehost(username, token);
            judgehostService.log(username, ip, "Login.");
            return BaseResponsePackageUtil.baseData(token);
        } else {
            judgehostService.log(username, ip, String.format("Failed login, password=%s", password));
            return ResponseConstant.X_USER_WRONG_PASSWORD;
        }
    }


    @RequestMapping(value = "/testcase", method = RequestMethod.POST)
    public ResponseEntity<FileSystemResource> downloadTestcase(
            @RequestParam(name = "judgehost", required = true) String judgehost,
            @RequestParam(name = "key", required = true) String key,
            @RequestParam(name = "id", required = true) int testcaseId,
            @RequestParam(name = "input", required = true) boolean isInput,
            @RequestParam(name = "md5", required = true) String md5,
            HttpServletRequest request
    ) {
        String ip = IpUtil.getIpAddr(request);
        String token = judgehostService.getToken(judgehost);
        if (!judgehostService.checkKey(token, key)) {
            judgehostService.log(judgehost, ip, String.format("Failed get testcase [700], key=%s", key));
            return ResponseEntity.status(700).build();
        }
        Testcase testcase = testcaseService.getTestcase(testcaseId);
        if (testcase == null) {
            judgehostService.log(judgehost, ip, String.format("Failed get testcase [404,db], testcaseId=%s", testcaseId));
            return ResponseEntity.notFound().build();
        }
        File file = new File(testcaseService.getTestcasePath(testcaseId, isInput));
        if (!file.exists()) {
            judgehostService.log(judgehost, ip, String.format("Failed get testcase [404,file], testcaseId=%s", testcaseId));
            return ResponseEntity.notFound().build();
        }

        // For judgehost: if the md5 of local data is the newest, return [NoContent]
        String newestMD5 = isInput?testcase.getInputMD5():testcase.getOutputMD5();
        if (md5.equals(newestMD5)) {
            judgehostService.log(judgehost, ip, String.format("Get testcase newest md5, testcaseId=%s, md5=%s", testcaseId, md5));
            return ResponseEntity.noContent().build();
        }

        judgehostService.log(judgehost, ip, String.format("Get testcase, testcaseId=%s, md5=%s", testcaseId, md5));
        HttpHeaders headers = HttpUtil.fileHeaders(testcaseId + (isInput?".in":".ans"));
        return ResponseEntity .ok() .headers(headers) .contentLength(file.length()) .contentType(MediaType.parseMediaType("application/octet-stream")) .body(new FileSystemResource(file));
    }

    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    public Map<String, Object> judge(
            @RequestParam(name = "judgehost", required = true) String judgehost,
            @RequestParam(name = "key", required = true) String key,
            @RequestParam(name = "solutionId", required = true) long solutionId,
            @RequestParam(name = "result", required = true) int result,
            @RequestParam(name = "memoryUsed", required = true) int memoryUsed,
            @RequestParam(name = "timeUsed", required = true) int timeUsed,
            @RequestParam(name = "remark", required = true) String remark,
            @RequestParam(name = "passTestcase", required = true) String passTestcase,
            HttpServletRequest request
    ) throws JsonProcessingException {
        String ip = IpUtil.getIpAddr(request);
        String token = judgehostService.getToken(judgehost);
        if (!judgehostService.checkKey(token, key)) {
            judgehostService.log(judgehost, ip, String.format("Failed judge [700], key=%s", key));
            return ResponseConstant.X_JUDGEHOST_DUE;
        }
        List<Integer> passTestcaseArray = Arrays.asList(objectMapper.readValue(passTestcase, Integer[].class));
        solutionService.updateSolutionResult(new SolutionResult(solutionId, result, memoryUsed, timeUsed, remark, judgehost, passTestcaseArray));
        judgehostService.log(judgehost, ip, String.format("Judged, solutionId=%s, result=%s, memoryUsed=%s, timeUsed=%s, remark=%s, judgehost=%s", solutionId, result, memoryUsed, timeUsed, remark, judgehost));
        return BaseResponsePackageUtil.succeedMessage();
    }

}
