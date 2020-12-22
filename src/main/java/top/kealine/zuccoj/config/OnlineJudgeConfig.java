package top.kealine.zuccoj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OnlineJudgeConfig {
    @Value("${zuccoj.upload.root}") public String uploadRoot;
    @Value("${zuccoj.upload.data}") public String dataDir;
    @Value("${zuccoj.upload.files}") public String filesDir;

    public final String separator = java.io.File.separator;

}
