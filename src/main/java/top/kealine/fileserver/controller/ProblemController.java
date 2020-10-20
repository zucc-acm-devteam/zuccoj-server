package top.kealine.fileserver.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.fileserver.service.ProblemService;
import top.kealine.fileserver.service.UserService;
import top.kealine.fileserver.util.BaseResponsePackageUtil;

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

    @RequestMapping("/get")
    public Map<String, Object> get(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "pageSize", required = true) int pageSize,
            HttpServletRequest request) {
        return BaseResponsePackageUtil.baseData(
                ImmutableMap.of(
                        "problems", problemService.getProblemsByPaging(page, pageSize),
                        "count", problemService.getProblemsCount(),
                        "page", page
                        ));
    }
}
