package top.kealine.zuccoj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import org.apache.ibatis.ognl.EnumerationIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.constant.SupportedLanguage;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.ProblemInfo;
import top.kealine.zuccoj.entity.Solution;
import top.kealine.zuccoj.entity.SolutionResult;
import top.kealine.zuccoj.entity.SolutionStatus;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.service.ContestService;
import top.kealine.zuccoj.service.ProblemService;
import top.kealine.zuccoj.service.SolutionService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solution")
public class SolutionController {
    private final UserService userService;
    private final ProblemService problemService;
    private final SolutionService solutionService;
    private final ContestService contestService;

    @Autowired
    SolutionController(UserService userService, ProblemService problemService, SolutionService solutionService, ContestService contestService) {
        this.userService = userService;
        this.problemService = problemService;
        this.solutionService = solutionService;
        this.contestService = contestService;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Map<String, Object> submitSolution(
            @RequestParam(name = "problemId", required = true) int problemId,
            @RequestParam(name = "lang", required = true) int lang,
            @RequestParam(name = "code", required = true) String code,
            HttpServletRequest request
    ) throws JsonProcessingException {
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }

        if (!SupportedLanguage.isLanguageSupported(lang)) {
            return ResponseConstant.X_LANGUAGE_NOT_SUPPORTED;
        }

        if (code.length() < 6) {
            return ResponseConstant.X_CODE_IS_TOO_SHORT;
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

        long solutionId = solutionService.newSolution(problemId, user.getUsername(), code, lang, 0);
        return BaseResponsePackageUtil.baseData(ImmutableMap.of("solutionId", solutionId));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> getSolution(
            @RequestParam(name = "solutionId", required = true) long solutionId,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Solution solution = solutionService.getSolutionById(solutionId);
        if (solution == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (user.isAdmin() || user.getUsername().equals(solution.getUsername())) {
            if (solution.getContestId() != 0) {
                solution.setProblemId(0);
            }
            return BaseResponsePackageUtil.baseData(solution);
        } else {
            return ResponseConstant.X_ACCESS_DENIED;
        }
    }

    @RequestMapping(value = "/rejudge", method = RequestMethod.POST)
    public Map<String, Object> rejudgeSolution(
            @RequestParam(name = "solutionId", required = true) long solutionId,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        if (solutionService.getSolutionById(solutionId) == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        try {
            solutionService.rejudgeSolution(solutionId);
            return BaseResponsePackageUtil.succeedMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponsePackageUtil.errorMessage();
        }
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public Map<String, Object> getResult(
            @RequestParam(name = "solutionId", required = true) long solutionId,
            HttpServletRequest request
    ) {
        SolutionResult result = solutionService.getSolutionResultById(solutionId);
        if (result == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        return BaseResponsePackageUtil.baseData(result);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Map<String, Object> getStatus(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "problemId", required = false) Integer problemId,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "lang", required = false) Integer lang,
            @RequestParam(name = "result", required = false) Integer result,
            @RequestParam(name = "judgehost", required = false) String judgehost,
            @RequestParam(name = "contestId", required = true) Integer contestId,
            HttpServletRequest request
    ) {
        if (offset == null) {
            offset = 0;
        }
        if (size == null) {
            size = 20;
        }

        // get real problemId in contest
        if (contestId != 0 && problemId != null) {
            ContestProblem contestProblem = contestService.getContestProblemByOrder(contestId, problemId);
            if (contestProblem == null) {
                return ResponseConstant.X_NOT_FOUND;
            }
            problemId = contestProblem.getProblemId();
        }

        List<SolutionStatus> statuses = solutionService.getSolutionStatus(offset, size, problemId, username, lang, result, judgehost, contestId);
        if (statuses == null) {
            return ResponseConstant.X_NOT_FOUND;
        }

        // hide real problemId in contest
        if (contestId != 0) {
            Map<Integer, ContestProblem> map = new HashMap<>();
            for (SolutionStatus solutionStatus: statuses) {
                ContestProblem contestProblem = map.getOrDefault(solutionStatus.getProblemId(), null);
                if (contestProblem == null) {
                    contestProblem = contestService.getContestProblemByProblemId(contestId, solutionStatus.getProblemId());
                    if (contestProblem == null) {
                        return ResponseConstant.X_NOT_FOUND;
                    }
                    map.put(solutionStatus.getProblemId(), contestProblem);
                }
                solutionStatus.setProblemId(contestProblem.getProblemOrder());
            }
        }

        return BaseResponsePackageUtil.baseData(statuses);
    }

    @RequestMapping(value = "/weekStat", method = RequestMethod.GET)
    public Map<String, Object> getStatIn7Days(
            @RequestParam(name = "username", required = false) String username,
            HttpServletRequest request
    ) {
        return BaseResponsePackageUtil.baseData(solutionService.get7DaysStat(username));
    }

    @RequestMapping(value = "/yearStat", method = RequestMethod.GET)
    public Map<String, Object> getStatIn1Year(
            @RequestParam(name = "username", required = true) String username,
            HttpServletRequest request
    ) {
        return BaseResponsePackageUtil.baseData(solutionService.getStat1YearStat(username));
    }
}
