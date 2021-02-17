package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.mapper.ScoreboardMapper;

@Service
public class ScoreboardService {
    public static final String REDIS_SCOREBOARD_KEY = "ZUCCOJ::SCOREBOARD";
    private final RedisTemplate<String, String> redisTemplate;
    private final ScoreboardMapper scoreboardMapper;

    @Autowired
    ScoreboardService(
            RedisTemplate<String, String> redisTemplate,
            ScoreboardMapper scoreboardMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.scoreboardMapper = scoreboardMapper;
    }

    public String getScoreboard(int contestId) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String json = hashOps.get(REDIS_SCOREBOARD_KEY, Integer.toString(contestId));
        if (json == null) {
            json = scoreboardMapper.getScoreboardFromDB(contestId);
            if (json == null) {
                json = "{}";
            }
        }
        return json;
    }
}
