package top.kealine.zuccoj.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ServerFileUtil {
    public static void save(MultipartFile multipartFile, String name) throws IOException {
        multipartFile.transferTo(new File(name));
    }
    public static void delete(String name) {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
    }
}
