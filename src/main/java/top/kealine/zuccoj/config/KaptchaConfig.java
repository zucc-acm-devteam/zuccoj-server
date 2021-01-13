package top.kealine.zuccoj.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDDefaultKaptcha() {
        DefaultKaptcha dk = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.font.color", "16,25,157");
        properties.setProperty("kaptcha.image.width", "72");
        properties.setProperty("kaptcha.image.height", "27");
        properties.setProperty("kaptcha.textproducer.font.size", "20");
        properties.setProperty("kaptcha.session.key", "zuccoj-captcha");
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        dk.setConfig(config);
        return dk;
    }
}