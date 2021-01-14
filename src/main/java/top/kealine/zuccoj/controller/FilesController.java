package top.kealine.zuccoj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kealine.zuccoj.constant.PermissionLevel;
import top.kealine.zuccoj.constant.ResponseConstant;
import top.kealine.zuccoj.service.FilesService;
import top.kealine.zuccoj.service.UserService;
import top.kealine.zuccoj.util.BaseResponsePackageUtil;
import top.kealine.zuccoj.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FilesController {
    private final FilesService filesService;
    private final UserService userService;

    @Autowired
    FilesController(FilesService filesService, UserService userService) {
        this.filesService = filesService;
        this.userService = userService;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> download(
            @RequestParam(name = "filename", required = true) String filename
    ) {
       if (filename == null) {
           return ResponseEntity.badRequest().build();
       }
       File file = new File(filesService.getFilePath(filename));
       if (!file.exists()) {
           return ResponseEntity.notFound().build();
       }
        return ResponseEntity
                .ok()
                .headers(HttpUtil.fileHeadersUUID(filename))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(
            @RequestParam(name = "file", required = true) MultipartFile file,
            HttpServletRequest request
    ) {
        if (!userService.checkUserPermission(request.getSession(), PermissionLevel.ADMIN)) {
            return ResponseConstant.X_ACCESS_DENIED;
        }
        try {
            return BaseResponsePackageUtil.baseData(filesService.newFile(file));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstant.X_SYSTEM_ERROR;
        }
    }
}
