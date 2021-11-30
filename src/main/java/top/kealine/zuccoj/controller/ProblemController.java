package top.kealine.zuccoj.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.Problem;
import top.kealine.zuccoj.entity.ProblemDisplay;
import top.kealine.zuccoj.entity.ProblemInfo;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.service.ProblemService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;
import top.kealine.zuccoj.util.ProblemUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/problem")
public class ProblemController {
    private final UserService userService;
    private final ProblemService problemService;

    @Autowired
    ProblemController(UserService userService, ProblemService problemService) {
        this.userService = userService;
        this.problemService = problemService;
    }

    @RequestMapping(value = "/getProblemListForAdmin", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getProblemListForAdmin(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "pageSize", required = true) int pageSize,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.baseData(
                ImmutableMap.of(
                        "problems", problemService.getProblemListForAdmin(page, pageSize),
                        "count", problemService.getProblemsCount(),
                        "page", page
                        ));
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Map<String, Object> newOne(
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "input", required = false) String input,
            @RequestParam(name = "output", required = false) String output,
            @RequestParam(name = "hint", required = false) String hint,
            @RequestParam(name = "timeLimit", required = true) int timeLimit,
            @RequestParam(name = "memoryLimit", required = true) int memoryLimit,
            @RequestParam(name = "spj", required = false) String spj,
            @RequestParam(name = "visible", required = true) boolean visible,
            @RequestParam(name = "samples", required = false) String samples,
            @RequestParam(name = "tags", required = false) String tags,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Problem problem = ProblemUtil.packageUp(-1, title, description, input, output, hint, timeLimit, memoryLimit, spj, visible, samples, tags);
        int problemId = problemService.newProblem(problem);
        return BaseResponsePackageUtil.baseData(
                ImmutableMap.of("problemId", problemId)
        );
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> getProblem(
            @RequestParam(name = "problemId", required = true) int problemId,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Problem problem = problemService.getProblem(problemId);
        if (problem == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        return BaseResponsePackageUtil.baseData(problem);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(
            @RequestParam(name = "problemId", required = true) int problemId,
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "input", required = false) String input,
            @RequestParam(name = "output", required = false) String output,
            @RequestParam(name = "hint", required = false) String hint,
            @RequestParam(name = "timeLimit", required = true) int timeLimit,
            @RequestParam(name = "memoryLimit", required = true) int memoryLimit,
            @RequestParam(name = "spj", required = false) String spj,
            @RequestParam(name = "samples", required = false) String samples,
            @RequestParam(name = "tags", required = false) String tags,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Problem problem = ProblemUtil.packageUp(problemId, title, description, input, output, hint, timeLimit, memoryLimit, spj, false, samples, tags);
        int cnt = problemService.updateProblem(problem);
        if (cnt == 0) {
            return ResponseConstant.X_NOT_FOUND;
        } else {
            return ResponseConstant.V_UPDATE_SUCCESS;
        }
    }

    @RequestMapping(value = "/visible", method = RequestMethod.POST)
    public Map<String, Object> updateVisible(
            @RequestParam(name = "problemId", required = true) int problemId,
            @RequestParam(name = "visible", required = true) boolean visible,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        int cnt = problemService.updateProblemVisible(problemId, visible);
        if (cnt == 0) {
            return ResponseConstant.X_NOT_FOUND;
        } else {
            return ResponseConstant.V_UPDATE_SUCCESS;
        }
    }

    @RequestMapping(value = "/getProblemSet", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getProblemSet(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "pageSize", required = true) int pageSize,
            HttpServletRequest request
    ) {
        User user = userService.getUserFromSession(request.getSession());
        boolean showAll = userService.checkUserPermission(user, PermissionLevel.ADMIN);
        String username = user == null ? null : user.getUsername();
        return BaseResponsePackageUtil.baseData(
                ImmutableMap.of(
                        "problems", problemService.getProblemInfoList(page, pageSize, showAll, username),
                        "count", problemService.getProblemInfoListCount(showAll),
                        "page", page
                ));
    }

    @RequestMapping(value = "/info", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getProblemInfo(
            @RequestParam(name = "problemId", required = true) int problemId,
            HttpServletRequest request
    ) {
        ProblemInfo info = problemService.getProblemInfo(problemId);
        if (info == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (!info.isVisible()) {
            if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
                return ResponseConstant.X_ACCESS_DENIED;
            }
        }
        return BaseResponsePackageUtil.baseData(info);
    }

    @RequestMapping(value = "/display", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getProblemDisplay(
            @RequestParam(name = "problemId", required = true) int problemId,
            HttpServletRequest request
    ) {
        ProblemDisplay display = problemService.getProblemDisplay(problemId);
        if (display == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        if (!display.isVisible()) {
            if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
                return ResponseConstant.X_ACCESS_DENIED;
            }
        }
        return BaseResponsePackageUtil.baseData(display);
    }

    @RequestMapping(value = "/title", method = RequestMethod.GET)
    public Map<String, Object> getProblemTitle(
            @RequestParam(name = "problemId", required = true) int problemId,
            HttpServletRequest request
    ) {
        String title = problemService.getProblemTitle(problemId);
        if (title == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        return BaseResponsePackageUtil.baseData(title);
    }

    @RequestMapping(value = "/searchProblemInfo", method = RequestMethod.GET)
    public Map<String, Object> searchProblemInfo(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "pageSize", required = true) int pageSize,
            @RequestParam(name = "keyWord") String keyWord,
            HttpServletRequest request
    ){
        User user = userService.getUserFromSession(request.getSession());
        boolean showAll = userService.checkUserPermission(user, PermissionLevel.ADMIN);
        String username = user == null ? null : user.getUsername();
        return BaseResponsePackageUtil.baseData(
                ImmutableMap.of(
                        "problems", problemService.searchProblemInfo(page,pageSize,showAll,keyWord,username),
                        "count", problemService.getSearchProblemInfoCount(showAll,keyWord),
                        "page", page
                ));
    }
}
