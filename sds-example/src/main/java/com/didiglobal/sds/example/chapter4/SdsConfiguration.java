package com.didiglobal.sds.example.chapter4;

import com.didiglobal.sds.aspectj.SdsPointAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: manzhizhen
 * @Date: Create in 2020-03-29 20:27
 */
@Configuration
public class SdsConfiguration {
    @Bean
    public SdsPointAspect createSdsPointAspect() {
        return new SdsPointAspect();
    }
}
