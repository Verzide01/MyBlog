package com.piggod.blogfront;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.piggod.blogfront","com.piggod.common"})    //扫描common模块下的包
@MapperScan("com.piggod.common.mapper")
public class BlogFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogFrontApplication.class, args);
    }

}
