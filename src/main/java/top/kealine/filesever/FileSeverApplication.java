package top.kealine.filesever;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.kealine.filesever.mapper")
public class FileSeverApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileSeverApplication.class, args);
    }

}
