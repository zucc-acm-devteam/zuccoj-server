package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.entity.Feedback;
import top.kealine.zuccoj.mapper.FeedbackMapper;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackMapper feedbackMapper;

    @Autowired
    FeedbackService(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    public void add(String username, String content) {
        feedbackMapper.add(new Feedback(username, content));
    }

    public void update(int id, boolean unread) {
        Feedback feedback = get(id);
        feedback.setUnread(unread);
        feedbackMapper.update(feedback);
    }

    public Feedback get(int id) {
        return feedbackMapper.get(id);
    }

    public List<Feedback> getByPage(int page, int pageSize) {
        return feedbackMapper.getByPage((page-1)*pageSize, pageSize);
    }

    public int count() {
        return feedbackMapper.count();
    }

    public int countUnread() {
        return feedbackMapper.countUnread();
    }
}
