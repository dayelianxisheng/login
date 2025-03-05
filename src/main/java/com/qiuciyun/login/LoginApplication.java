package com.qiuciyun.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.qiuciyun.login.mapper")
@ComponentScan(basePackages = {
    "com.qiuciyun.login.config",
    "com.qiuciyun.login.utils",
    "com.qiuciyun.login.controller",
    "com.qiuciyun.login.service"
})
public class LoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

}
