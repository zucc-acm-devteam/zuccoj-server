package top.kealine.zuccoj.util;

import com.google.common.collect.ImmutableMap;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZipUtil {
    public static Map<String, String> unzipTestcasePackage(String fullPath) throws Exception {
        String separator = java.io.File.separator;
        if (!ServerFileUtil.isExtendWith(fullPath, ".zip")) {
            throw new Exception("this is not a zip file!");
        }
        String dir = ServerFileUtil.getFilenameWithoutExtend(fullPath);
        if (!dir.endsWith(separator)) {
            dir+=separator;
        }
        File targetDir = new File(dir);
        if (targetDir.exists()) {
            if(!targetDir.delete()) {
                throw new Exception(String.format("cannot delete %s", dir));
            }
        }
        if (!targetDir.mkdir()) {
            throw new Exception(String.format("cannot mkdir %s", dir));
        }

        ZipFile zipFile = new ZipFile(fullPath);
        List<FileHeader> headers = zipFile.getFileHeaders();
        List<FileHeader> inputHeaders = headers
                .stream()
                .filter(header -> ServerFileUtil.isExtendWith(header.getFileName(), ".in"))
                .collect(Collectors.toList());
        ImmutableMap.Builder<String, String> result = ImmutableMap.builder();

        try {
            for (FileHeader inputHeader: inputHeaders) {
                String filename = ServerFileUtil.getFilenameWithoutExtend(inputHeader.getFileName());
                FileHeader ans = zipFile.getFileHeader(filename + ".ans");
                FileHeader out = zipFile.getFileHeader(filename + ".out");
                FileHeader choice = (ans!=null?ans:out); // .ans first
                String outputExt = (ans!=null?".ans":".out");
                if (choice != null) {
                    String inputFilePath = dir + filename + ".in";
                    String outputFilePath = dir + filename + ".ans";
                    ServerFileUtil.save(zipFile.getInputStream(inputHeader), inputFilePath);
                    ServerFileUtil.save(zipFile.getInputStream(choice), outputFilePath);
                    result.put(filename, outputExt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.build();
    }
}
