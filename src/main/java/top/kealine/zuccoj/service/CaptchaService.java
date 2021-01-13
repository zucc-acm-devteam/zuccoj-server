package top.kealine.zuccoj.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CaptchaService {
    private final String USER_CAPTCHA_CODE_KEY = "USER_CAPTCHA_CODE";
    private final DefaultKaptcha defaultKaptcha;

    @Autowired
    CaptchaService(DefaultKaptcha defaultKaptcha) {
        this.defaultKaptcha = defaultKaptcha;
    }

    public byte[] getCaptcha(HttpSession session) throws IOException {
        String createText = defaultKaptcha.createText();
        session.setAttribute(USER_CAPTCHA_CODE_KEY, createText);
        BufferedImage bi = defaultKaptcha.createImage(createText);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", out);
        return out.toByteArray();
    }

    public boolean checkCaptcha(HttpSession session, String userInput) {
        if (userInput == null) {
            return false;
        }
        String rightAns = (String) session.getAttribute(USER_CAPTCHA_CODE_KEY);
        return userInput.equalsIgnoreCase(rightAns);
    }
}
