package top.kealine.fileserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.fileserver.literal.PermissionLevel;
import top.kealine.fileserver.service.TestcaseService;
import top.kealine.fileserver.service.UserService;
import top.kealine.fileserver.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
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
            return PermissionLevel.ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.baseData(testcaseService.getTestcaseByProblemId(problemId));
    }
}
