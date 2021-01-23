package top.kealine.zuccoj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.Contest;
import top.kealine.zuccoj.service.ContestService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/contest")
public class ContestController {
    private final ContestService contestService;
    private final UserService userService;

    @Autowired
    ContestController(ContestService contestService, UserService userService) {
        this.contestService = contestService;
        this.userService = userService;
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

}
