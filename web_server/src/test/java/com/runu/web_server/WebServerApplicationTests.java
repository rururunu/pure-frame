package com.runu.web_server;

import com.runu.web_server.tool.EncryptionHMACSHA265;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class WebServerApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(EncryptionHMACSHA265.HMACSHA256_Base64("admin123."));
    }

    @Test
    void password() {
        String password = "admin123.";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        System.out.println("加密后的密码:" + encodedPassword);
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("比较结果:" + matches);
    }

}
