package top.kealine.zuccoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kealine.zuccoj.config.OnlineJudgeConfig;
import top.kealine.zuccoj.util.ServerFileUtil;

import java.io.IOException;
import java.util.UUID;

@Service
public class FilesService {
    private final OnlineJudgeConfig onlineJudgeConfig;

    @Autowired
    FilesService(OnlineJudgeConfig onlineJudgeConfig) {
        this.onlineJudgeConfig = onlineJudgeConfig;
    }

    public String getFilePath(String fullName) {
        return onlineJudgeConfig.filesDir + fullName;
    }

    public String getFilePath(String name, String ext) {
        return getFilePath( name + ext);
    }

    public String newFile(MultipartFile file) throws IOException {
        String ext = ServerFileUtil.getFileExtend(file.getOriginalFilename());
        String uuidName = UUID.randomUUID().toString();
        String fullPath = getFilePath(uuidName, ext);
        ServerFileUtil.save(file, fullPath);
        return uuidName + ext;
    }
}
