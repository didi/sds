package com.didiglobal.sds.extension.spring.boot.autoconfigure;


import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * 将SDS和Spring Boot的自动配置打通
 */
@EnableConfigurationProperties({SdsProperties.class})
public class SdsAutoConfiguration {

    @Autowired
    private SdsProperties sdsProperties;

    @Bean
    public SdsClient getOrCreateSdsClient() {
        if (sdsProperties == null) {
            throw new IllegalStateException("SdsAutoConfiguration#getOrCreateSdsClient 无法解析到sds.app-group-name、" +
                    "sds.app-name和sds.server-addr-list等properties配置，无法创建SdsClient");
        }

        return SdsClientFactory.getOrCreateSdsClient(sdsProperties.getAppGroupName(), sdsProperties.getAppName(),
                sdsProperties.getServerAddrList());
    }
}
