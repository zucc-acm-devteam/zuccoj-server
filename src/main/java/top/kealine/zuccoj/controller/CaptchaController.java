package top.kealine.zuccoj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.kealine.zuccoj.service.CaptchaService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    private final CaptchaService captchaService;

    @Autowired
    CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public void get(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(captchaService.getCaptcha(request.getSession()));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}
