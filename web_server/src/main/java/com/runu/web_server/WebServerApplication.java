package com.runu.web_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.runu.web_server.mapper")
@EnableTransactionManagement
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }
}
