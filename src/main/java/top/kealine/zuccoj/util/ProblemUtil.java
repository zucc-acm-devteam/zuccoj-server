package top.kealine.zuccoj.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.kealine.zuccoj.entity.Problem;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProblemUtil {
    public static Problem packageUp(
            int problemId,
            String title,
            String description,
            String input,
            String output,
            String hint,
            int timeLimit,
            int memoryLimit,
            String spj,
            boolean visible,
            String samples,
            String tags
    ) {
        Problem problem = new Problem();
        if (problemId != -1) {
            problem.setProblemId(problemId);
        }
        problem.setTitle(title);
        problem.setDescription(description);
        problem.setInput(input);
        problem.setOutput(output);
        problem.setHint(hint);
        problem.setTimeLimit(timeLimit);
        problem.setMemoryLimit(memoryLimit);
        problem.setSpj(spj);
        problem.setVisible(visible);
        problem.setSamples(samples);
        problem.setTags(tags);
        problem.setIsPolygon(0);
        return problem;
    }

    public static String getStringFromInputStream(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream, "Utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        reader.close();
        return sb.toString();
    }

    // find the FileHeader of check.cpp
    public static FileHeader checkIsSpj(String fullPath) throws Exception {
        ZipFile zipFile = new ZipFile(fullPath);
        List<FileHeader> headers = zipFile.getFileHeaders();
        return headers.stream().filter(fileHeader -> fileHeader.getFileName().equals("check.cpp")).findFirst().orElse(null);
    }

    public static FileHeader getProblemLatexHeader(String fullPath) throws Exception{
        ZipFile zipFile = new ZipFile(fullPath);
        List<FileHeader> headers = zipFile.getFileHeaders();

        return headers.stream().filter(
                fileHeader -> fileHeader.getFileName().equals("statements/.html/english/problem.html")
        ).findFirst().orElse(null);
    }

    public static Problem packageUpByZip(String fullPath, String title) throws Exception {
        String dir = ZipUtil.getDirPath(fullPath);
        ZipUtil.mkdirDir(dir);
        ZipFile zipFile = new ZipFile(fullPath);
        List<FileHeader> headers = zipFile.getFileHeaders();
        FileHeader problemJsonFile = headers.stream().filter(headerFile -> headerFile.getFileName().contains("problem-properties")).findFirst().orElse(null);
        if (problemJsonFile == null) throw new Exception("there is no json of problem is find from zip!");
        ZipInputStream inputStream = zipFile.getInputStream(problemJsonFile);
        String problemJson = getStringFromInputStream(inputStream);

        // 获取题目时间、空间限制
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMapDes = mapper.readValue(problemJson, Map.class);
        Problem problem = new Problem();
        problem.setTitle((String) jsonMapDes.get("name"));
        problem.setTimeLimit((Integer) jsonMapDes.get("timeLimit"));
        problem.setMemoryLimit((Integer) jsonMapDes.get("memoryLimit"));

        // 获取spj
        FileHeader checkHeader = checkIsSpj(fullPath);
        if (checkHeader != null) problem.setSpj(getStringFromInputStream(zipFile.getInputStream(checkHeader)));
        problem.setVisible(false);
        problem.setIsPolygon(1);

        // 获取题面latex文档
        FileHeader latexHeader = getProblemLatexHeader(fullPath);
        if (latexHeader == null) throw new Exception("there is no problem.latex find from zip");
        ZipInputStream latexInputStream = zipFile.getInputStream(latexHeader);
        String latex = getStringFromInputStream(latexInputStream);
        Document document = Jsoup.parse(latex);
        problem.setDescription(document.body().toString());


        return problem;
    }
}


