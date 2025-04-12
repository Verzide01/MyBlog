package com.piggod.blogfront;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"com.piggod.blogfront","com.piggod.common"})    //扫描common模块下的包
@MapperScan("com.piggod.common.mapper")
@EnableScheduling //@EnableScheduling是spring提供的定时任务的注解
//@EnableSwagger2 // 开启swagger接口文档
public class BlogFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogFrontApplication.class, args);
    }

}
