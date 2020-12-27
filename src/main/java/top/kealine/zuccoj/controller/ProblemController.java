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

    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> get(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "pageSize", required = true) int pageSize,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.baseData(
                ImmutableMap.of(
                        "problems", problemService.getProblemsByPaging(page, pageSize),
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
        problemService.newProblem(problem);
        return BaseResponsePackageUtil.baseData(problem);
    }
}
