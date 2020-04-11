package com.didiglobal.sds.example.chapter4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot启动类
 *
 * @Author: manzhizhen
 * @Date: Create in 2020-03-29 14:31
 */
@SpringBootApplication
public class SpringBoootWithSds {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringBoootWithSds.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
