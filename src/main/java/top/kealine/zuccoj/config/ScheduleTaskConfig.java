package top.kealine.zuccoj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.kealine.zuccoj.service.ScoreboardRunner;

@Component
@EnableScheduling
@EnableAsync
// Multithreaded Schedule Task
public class ScheduleTaskConfig {

    @Autowired
    private ScoreboardRunner scoreboardRunner;

    @Async
    @Scheduled(fixedDelay = 1000 * 10)
    public void calculateScoreboard() {

    }

}
