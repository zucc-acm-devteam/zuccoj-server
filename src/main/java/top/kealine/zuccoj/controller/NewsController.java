package top.kealine.zuccoj.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.service.NewsService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {
    private final UserService userService;
    private final NewsService newsService;

    NewsController(UserService userService, NewsService newsService) {
        this.newsService = newsService;
        this.userService = userService;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Map<String, Object> newNews(
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "content", required = true) String content,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        newsService.newNews(title, content);
        return ResponseConstant.V_ADD_SUCCESS;
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> allNews(
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        return BaseResponsePackageUtil.baseData(newsService.getNewsList());
    }

    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public Map<String, Object> displayNews() {
        return BaseResponsePackageUtil.baseData(newsService.getNewsDisplayList());
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateNews(
            @RequestParam(name = "newsId", required = true) int newsId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "content", required = false) String content,
            @RequestParam(name = "visible", required = false) Boolean visible,
            @RequestParam(name = "priority", required = false) Integer priority,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        newsService.updateNews(newsId, title, content, visible, priority);
        return ResponseConstant.V_UPDATE_SUCCESS;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteNews(
            @RequestParam(name = "newsId", required = true) int newsId,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        newsService.deleteNews(newsId);
        return ResponseConstant.V_DELETE_SUCCESS;
    }

}
