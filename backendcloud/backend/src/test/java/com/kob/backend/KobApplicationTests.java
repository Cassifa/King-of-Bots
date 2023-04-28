package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// 主要是下面这行注解
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KobApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123"));
        System.out.println(passwordEncoder.matches("123","$2a$10$kwzYw92JYAhANMXoc8kH5epp95PsTBlaLcvckAVJ77cgzNAfNqXoK"));
        String password="123";
        String encodedPassword=passwordEncoder.encode(password);
        System.out.println(encodedPassword);
   }

}
