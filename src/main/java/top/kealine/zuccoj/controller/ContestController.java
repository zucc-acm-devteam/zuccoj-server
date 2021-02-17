package top.kealine.zuccoj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.constant.SupportedLanguage;
import top.kealine.zuccoj.entity.Contest;
import top.kealine.zuccoj.entity.ContestInfo;
import top.kealine.zuccoj.entity.ContestProblem;
import top.kealine.zuccoj.entity.ContestProblemInfo;
import top.kealine.zuccoj.entity.ProblemDisplay;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.service.ContestService;
import top.kealine.zuccoj.service.ProblemService;
import top.kealine.zuccoj.service.ScoreboardService;
import top.kealine.zuccoj.service.SolutionService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/contest")
public class ContestController {
    private final ContestService contestService;
    private final ProblemService problemService;
    private final SolutionService solutionService;
    private final UserService userService;
    private final ScoreboardService scoreboardService;

    @Autowired
    ContestController(
            ContestService contestService,
            ProblemService problemService,
            SolutionService solutionService,
            UserService userService,
            ScoreboardService scoreboardService
    ) {
        this.contestService = contestService;
        this.userService = userService;
        this.problemService = problemService;
        this.solutionService = solutionService;
        this.scoreboardService = scoreboardService;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Map<String, Object> newContest(
            @RequestParam(name = "contestName", required = true) String contestName,
            @RequestParam(name = "beginTime", required = true) String beginTime,
            @RequestParam(name = "endTime", required = true) String endTime,
            @RequestParam(name = "freezeTime", required = false) String freezeTime,
            @RequestParam(name = "unfreezeTime", required = false) String unfreezeTime,
            @RequestParam(name = "isPublic", required = true) boolean isPublic,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "contestType", required = true) int contestType,
            @RequestParam(name = "problems", required = true) String problems,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        if (password == null) {
            password = "";
        }
        Contest contest = new Contest(contestName, beginTime, endTime, freezeTime, unfreezeTime, isPublic, password, contestType);
        try {
            return BaseResponsePackageUtil.baseData(contestService.newContest(contest, problems));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstant.X_ADD_FAILED;
        }
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> getContest(
            @RequestParam(name = "contestId", required = true) int contestId,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Contest contest = contestService.getContest(contestId);
        if (contest == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        List<ContestProblem> contestProblems = contestService.getContestProblem(contestId);
        List<Map> contestProblemsTitle = contestProblems
                .stream()
                .map(o -> ImmutableMap.of(
                        "problemId", o.getProblemId(),
                        "problemTitle", problemService.getProblemTitle(o.getProblemId())
                        )
                )
                .collect(toList());
        return BaseResponsePackageUtil.baseData(ImmutableMap.of(
                "contest", contest,
                "problems", contestProblemsTitle
        ));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateContest(
            @RequestParam(name = "contestId", required = true) int contestId,
            @RequestParam(name = "contestName", required = true) String contestName,
            @RequestParam(name = "beginTime", required = true) String beginTime,
            @RequestParam(name = "endTime", required = true) String endTime,
            @RequestParam(name = "freezeTime", required = false) String freezeTime,
            @RequestParam(name = "unfreezeTime", required = false) String unfreezeTime,
            @RequestParam(name = "isPublic", required = true) boolean isPublic,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "contestType", required = true) int contestType,
            @RequestParam(name = "problems", required = true) String problems,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Contest oldContest = contestService.getContest(contestId);
        if (oldContest == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (password == null) {
            password = "";
        }
        Contest contest = new Contest(contestId, contestName, beginTime, endTime, freezeTime, unfreezeTime, isPublic, password, contestType);
        try {
            contestService.updateContest(contest, problems);
            return ResponseConstant.V_UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstant.X_UPDATE_FAILED;
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> getContestInfoList(
            @RequestParam(name = "page", required = true) Integer page,
            @RequestParam(name = "pageSize", required = true) Integer pageSize,
            HttpServletRequest request
    ) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        User user = userService.getUserFromSession(request.getSession());
        List<ContestInfo> contestInfos = contestService.getContestInfoList(user==null?null:user.getUsername(), page, pageSize);
        if (userService.checkUserPermission(user, PermissionLevel.ADMIN)) {
            contestInfos.forEach(info -> info.setStatus(1));
        }
        return BaseResponsePackageUtil.baseData(ImmutableMap.of(
                "data", contestInfos,
                "page", page,
                "pageSize", pageSize,
                "count", contestService.getContestCount()
        ));
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Map<String, Object> getContestInfo(
            @RequestParam(name = "contestId", required = true) Integer contestId,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        ContestInfo contestInfo = contestService.getContestInfo((user==null?null:user.getUsername()), contestId);
        if (contestInfo == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (user == null && contestInfo.getStatus() == 0) {
            contestInfo.setStatus(-1);
        }
        if (user != null && user.isAdmin()) {
            contestInfo.setStatus(1);
        }
        return BaseResponsePackageUtil.baseData(contestInfo);
    }

    @RequestMapping(value = "/member", method = RequestMethod.GET)
    public Map<String, Object> getContestMember(
            @RequestParam(name = "contestId", required = true) int contestId,
            HttpServletRequest request
    ) {
        Contest contest = contestService.getContest(contestId);
        if (contest == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        return BaseResponsePackageUtil.baseData(contestService.getContestMember(contestId, contest.isPublic()));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> registerPrivateContest(
            @RequestParam(name = "contestId", required = true) int contestId,
            @RequestParam(name = "password", required = true) String password,
            HttpServletRequest request
    ) {
        Contest contest = contestService.getContest(contestId);
        if (contest == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (contest.isPublic()) {
            return ResponseConstant.X_CONTEST_IS_NOT_PRIVATE;
        }
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }
        if (!contest.getPassword().equals(password)) {
            return ResponseConstant.X_CONTEST_WRONG_PASSWORD;
        } else {
            contestService.newContestMember(contestId, user.getUsername());
            return ResponseConstant.V_CONTEST_REGISTER_SUCCESS;
        }
    }

    @RequestMapping(value = "/problems", method = RequestMethod.GET)
    public Map<String, Object> getContestProblemInfoList(
            @RequestParam(name = "contestId", required = true) int contestId,
            HttpServletRequest request
    ) {
        Contest contest = contestService.getContest(contestId);
        if (contest == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        User user = userService.getUserFromSession(request.getSession());
        if (!contest.isPublic() && ((user == null) || (!user.isAdmin() && !contestService.isMemberOfContest(contestId, user.getUsername())))) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        List<ContestProblemInfo> contestProblemInfoList = contestService.getContestProblemInfoList(contestId, user == null ? null : user.getUsername());
        int contestStatus = contestService.getContestStatus(contestId);
        if (contestStatus < 1) {
            contestProblemInfoList.forEach(ContestProblemInfo::hideProblemId);
        }
        return BaseResponsePackageUtil.baseData(contestProblemInfoList);
    }

    @RequestMapping(value = "/problem/display", method = RequestMethod.GET)
    public Map<String, Object> getContestProblemDisplay(
            @RequestParam(name = "contestId", required = true) int contestId,
            @RequestParam(name = "problemId", required = true) int problemOrder, // problemId in contest -> problemOrder
            HttpServletRequest request
    ) {
        Contest contest = contestService.getContest(contestId);
        if (contest == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        int contestStatus = contestService.getContestStatus(contestId);
        User user = userService.getUserFromSession(request.getSession());
        ContestProblem contestProblem = contestService.getContestProblemByOrder(contestId, problemOrder);
        if (contestProblem == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (!contest.isPublic() && ((user == null) || (!user.isAdmin() && !contestService.isMemberOfContest(contestId, user.getUsername())))) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        if (contestStatus < 0 && (user == null || !user.isAdmin())) {
            return ResponseConstant.X_CONTEST_HAS_NOT_STARTED;
        }
        ProblemDisplay problemDisplay = problemService.getProblemDisplay(contestProblem.getProblemId());
        problemDisplay.setProblemId(problemOrder);
        return BaseResponsePackageUtil.baseData(problemDisplay);
    }

    @RequestMapping(value = "/solution/submit", method = RequestMethod.POST)
    public Map<String, Object> submitContestProblemSolution(
            @RequestParam(name = "contestId", required = true) int contestId,
            @RequestParam(name = "problemId", required = true) int problemOrder, // problemId in contest -> problemOrder
            @RequestParam(name = "lang", required = true) int lang,
            @RequestParam(name = "code", required = true) String code,
            HttpServletRequest request
    ) throws JsonProcessingException {
        if (code.length() < 6) {
            return ResponseConstant.X_CODE_IS_TOO_SHORT;
        }

        if (!SupportedLanguage.isLanguageSupported(lang)) {
            return ResponseConstant.X_LANGUAGE_NOT_SUPPORTED;
        }

        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }

        Contest contest = contestService.getContest(contestId);
        if (contest == null) {
            return ResponseConstant.X_NOT_FOUND;
        }

        if (!contest.isPublic() && (!user.isAdmin() && !contestService.isMemberOfContest(contestId, user.getUsername()))) {
            return ResponseConstant.X_ACCESS_DENIED;
        }

        ContestProblem contestProblem = contestService.getContestProblemByOrder(contestId, problemOrder);
        if (contestProblem == null) {
            return ResponseConstant.X_NOT_FOUND;
        }

        int contestStatus = contestService.getContestStatus(contestId);
        if (contestStatus < 0 && !user.isAdmin()) {
            return ResponseConstant.X_CONTEST_HAS_NOT_STARTED;
        }

        long solutionId = solutionService.newSolution(contestProblem.getProblemId(), user.getUsername(), code, lang, contestId);
        return BaseResponsePackageUtil.baseData(ImmutableMap.of("solutionId", solutionId));
    }

    @RequestMapping(value = "/scoreboard", method = RequestMethod.GET)
    public Map<String, Object> getScoreboard(
            @RequestParam(name = "contestId", required = true) int contestId,
            @RequestParam(name = "real", required = false) Boolean real,
            HttpServletRequest request
    ) {
        //TODO: real scoreboard for admin
        if (real != null && real) {
            if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
                return ResponseConstant.X_ACCESS_DENIED;
            }
        }
        return BaseResponsePackageUtil.baseData(scoreboardService.getScoreboard(contestId));
    }
}
