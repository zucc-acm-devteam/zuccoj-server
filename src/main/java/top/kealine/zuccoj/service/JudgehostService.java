package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.kealine.zuccoj.entity.Judgehost;
import top.kealine.zuccoj.mapper.JudgehostMapper;
import top.kealine.zuccoj.util.PasswordUtil;

import java.util.concurrent.TimeUnit;

@Service
public class JudgehostService {
    private final String JUDGEHOST_TOKEN_PREFIX = "ZUCCOJ::JUDGEHOST::TOKEN::";
    private final JudgehostMapper judgehostMapper;
    private final RedisTemplate<String,String> redisTemplate;

    @Autowired
    JudgehostService(JudgehostMapper judgehostMapper, RedisTemplate<String,String> redisTemplate) {
        this.judgehostMapper = judgehostMapper;
        this.redisTemplate = redisTemplate;
    }

    public Judgehost getJudgehostByName(String name) {
        return judgehostMapper.getJudgehostByName(name);
    }

    public boolean checkJudgehostPassword(Judgehost judgehost, String password) {
        if (judgehost == null) {
            return false;
        }
        String userInput = judgehost.getJudgehostUsername() + password;
        return PasswordUtil.check(judgehost.getJudgehostPassword(), userInput);
    }

    public void assignTokenToJudgehost(String username, String token) {
        ValueOperations<String,String> ops = redisTemplate.opsForValue();
        ops.set(JUDGEHOST_TOKEN_PREFIX + username, token, 1, TimeUnit.DAYS);
    }

    public String getToken(String username) {
        ValueOperations<String,String> ops = redisTemplate.opsForValue();
        return ops.get(JUDGEHOST_TOKEN_PREFIX + username);
    }

    public boolean checkKey(String token, String key) {
        if (token == null || key == null) {
            return false;
        }
        return PasswordUtil.checkToken(token, key);
    }

    public void newJudgehost(String name, String password) {
        String newPassword = PasswordUtil.encrypt(name+password);
        judgehostMapper.newJudgehost(name, newPassword);
    }

    public void log(String name, String ip, String description) {
        judgehostMapper.log(name, ip, description);
    }
}
