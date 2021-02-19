package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.Feedback;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    @Insert("INSERT INTO feedback(username, content) VALUES(#{username}, #{content})")
    void add(Feedback feedback);

    @Update("UPDATE feedback SET unread=#{unread} WHERE feedback_id=#{feedbackId}")
    void update(Feedback feedback);

    @Select("SELECT feedback_id feedbackId, username, content, create_time createTime, unread FROM feedback WHERE feedback_id=#{id}")
    Feedback get(int id);

    @Select("SELECT feedback_id feedbackId, username, content, create_time createTime, unread FROM feedback ORDER BY unread DESC,create_time LIMIT #{offset}, #{size}")
    List<Feedback> getByPage(int offset, int size);

    @Select("SELECT COUNT(*) FROM feedback")
    int count();

    @Select("SELECT COUNT(*) FROM feedback WHERE unread=true")
    int countUnread();
}
