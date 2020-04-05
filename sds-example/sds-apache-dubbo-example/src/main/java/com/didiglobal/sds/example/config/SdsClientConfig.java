package com.didiglobal.sds.example.config;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.extension.dubbo.DubboSdsClient;
import com.didiglobal.sds.extension.spring.boot.autoconfigure.SdsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sea on 2020-03-29.
 */
@Configuration(proxyBeanMethods = false)
public class SdsClientConfig {

    @Bean
    public SdsClient sdsClient(SdsProperties sdsProperties) {
        return new DubboSdsClient(sdsProperties.getAppGroupName(), sdsProperties.getAppName(), sdsProperties.getServerAddrList());
    }
}
