package top.kealine.zuccoj.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.constant.SupportedLanguage;
import top.kealine.zuccoj.entity.ProblemInfo;
import top.kealine.zuccoj.entity.Solution;
import top.kealine.zuccoj.entity.SolutionResult;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.service.ProblemService;
import top.kealine.zuccoj.service.SolutionService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/solution")
public class SolutionController {
    private final UserService userService;
    private final ProblemService problemService;
    private final SolutionService solutionService;

    @Autowired
    SolutionController(UserService userService, ProblemService problemService, SolutionService solutionService) {
        this.userService = userService;
        this.problemService = problemService;
        this.solutionService = solutionService;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Map<String, Object> submitSolution(
            @RequestParam(name = "problemId", required = true) int problemId,
            @RequestParam(name = "lang", required = true) int lang,
            @RequestParam(name = "code", required = true) String code,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }

        if (!SupportedLanguage.isLanguageSupported(lang)) {
            return ResponseConstant.X_LANGUAGE_NOT_SUPPORTED;
        }

        ProblemInfo problemInfo = problemService.getProblemInfo(problemId);
        if (problemInfo == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (!problemInfo.isVisible()) {
            if (!userService.checkUserPermission(user, PermissionLevel.ADMIN)) {
                return ResponseConstant.X_ACCESS_DENIED;
            }
        }

        long solutionId = solutionService.newSolution(problemId, user.getUsername(), code, lang);
        return BaseResponsePackageUtil.baseData(ImmutableMap.of("solutionId", solutionId));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> getSolution(
            @RequestParam(name = "solutionId", required = true) int solutionId,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }
        Solution solution = solutionService.getSolutionById(solutionId);
        if (solution == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (user.isAdmin() || user.getUsername().equals(solution.getUsername())) {
            return BaseResponsePackageUtil.baseData(solution);
        } else {
            return ResponseConstant.X_ACCESS_DENIED;
        }
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public Map<String, Object> getResult(
            @RequestParam(name = "solutionId", required = true) int solutionId,
            HttpServletRequest request
    ) {
        SolutionResult result = solutionService.getSolutionResultById(solutionId);
        if (result == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        return BaseResponsePackageUtil.baseData(result);
    }
}
