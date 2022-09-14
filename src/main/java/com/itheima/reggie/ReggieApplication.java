package com.itheima.reggie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.springframework.boot.SpringApplication.run;


@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching
@Slf4j
public class ReggieApplication {
    public static void main(String[] args) {
        run(ReggieApplication.class,args);

        log.info("项目启动成功");
    }

}
