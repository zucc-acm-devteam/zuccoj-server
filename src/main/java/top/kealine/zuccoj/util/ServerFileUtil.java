package top.kealine.zuccoj.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ServerFileUtil {
    public static String md5Hex(MultipartFile multipartFile) throws IOException {
        return md5Hex(multipartFile.getInputStream());
    }

    public static String md5Hex(InputStream inputStream) throws IOException {
        return DigestUtils.md5Hex(inputStream);
    }

    public static void save(MultipartFile multipartFile, String name) throws IOException {
        multipartFile.transferTo(new File(name));
    }
    public static void delete(String name) {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
    }

    public static String getFileExtend(String fullName) {
        if (fullName == null) {
            return "";
        }
        int pos = fullName.lastIndexOf('.');
        if (pos < 0) {
            return "";
        } else {
            return fullName.substring(pos);
        }
    }
}
