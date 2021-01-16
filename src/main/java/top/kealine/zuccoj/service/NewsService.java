package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.entity.News;
import top.kealine.zuccoj.entity.NewsDisplay;
import top.kealine.zuccoj.mapper.NewsMapper;

import java.util.List;

@Service
public class NewsService {
    private final NewsMapper newsMapper;

    @Autowired
    NewsService(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    public void newNews(String title, String content) {
        newsMapper.newNews(title, content);
    }

    public List<News> getNewsList() {
        return newsMapper.getNewsList();
    }

    public List<NewsDisplay> getNewsDisplayList() {
        return newsMapper.getNewsDisplayList();
    }

    public void updateNews(int newsId, String title, String content, Boolean visible, Integer priority) {
        newsMapper.updateNews(newsId, title, content, visible, priority);
    }

    public void deleteNews(int newsId) {
        newsMapper.deleteNews(newsId);
    }
}
