package top.kealine.zuccoj.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kealine.zuccoj.entity.News;
import top.kealine.zuccoj.entity.NewsDisplay;

import java.util.List;

@Mapper
public interface NewsMapper {
    @Insert("INSERT INTO news(title, content, visible, update_time) VALUES(#{title}, #{content}, false, NOW())")
    void newNews(String title, String content);

    @Select("SELECT news_id newsId, title, content, visible, priority, update_time updateTime, create_time createTime \n" +
            "FROM news ORDER BY priority DESC")
    List<News> getNewsList();

    @Select("SELECT title, content FROM news WHERE visible = true ORDER BY priority DESC")
    List<NewsDisplay> getNewsDisplayList();

    @Delete("DELETE FROM news WHERE news_id=#{newsId}")
    void deleteNews(int newsId);

    @Update("<script> UPDATE news SET update_time=NOW() \n" +
            "<if test=\"title != null\"> , title=#{title} </if> \n" +
            "<if test=\"content != null\"> , content=#{content} </if> \n" +
            "<if test=\"visible != null\"> , visible=#{visible} </if> \n" +
            "<if test=\"priority != null\"> , priority=#{priority} </if> \n" +
            "WHERE news_id=#{newsId} </script>")
    void updateNews(int newsId, String title, String content, Boolean visible, Integer priority);
}
