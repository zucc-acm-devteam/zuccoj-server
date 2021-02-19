package top.kealine.zuccoj.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.entity.Feedback;
import top.kealine.zuccoj.entity.User;
import top.kealine.zuccoj.service.FeedbackService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final UserService userService;

    FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public Map<String, Object> getUnreadCount(
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.baseData(feedbackService.countUnread());
    }


    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Map<String, Object> newFeedback(
            @RequestParam(value = "content", required = true) String content,
            HttpServletRequest request
    ) {
        if (content.length() > 1000) {
            return ResponseConstant.X_BAD_REQUEST;
        }
        User user = userService.getUserFromSession(request.getSession());
        if (user == null) {
            return ResponseConstant.X_USER_LOGIN_FIRST;
        }
        feedbackService.add(user.getUsername(), content);
        return ResponseConstant.V_ADD_SUCCESS;
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Map<String, Object> getFeedbacks(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        List<Feedback> feedbacks = feedbackService.getByPage(page, pageSize);
        return BaseResponsePackageUtil.baseData(
                ImmutableMap.of(
                        "data", feedbacks,
                        "count", feedbackService.count(),
                        "page", page,
                        "pageSize", pageSize
                )
        );
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateFeedback(
            @RequestParam(value = "feedbackId", required = true) int feedbackId,
            @RequestParam(value = "unread", required = true) boolean unread,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        Feedback feedback = feedbackService.get(feedbackId);
        if (feedback == null) {
            return ResponseConstant.X_NOT_FOUND;
        }
        feedbackService.update(feedbackId, unread);
        return ResponseConstant.V_ADD_SUCCESS;
    }
}
