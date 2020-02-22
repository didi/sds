package com.didiglobal.sds.extension.spring.boot.autoconfigure;


import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;


/**
 * 将SDS和Spring Boot的自动配置打通
 */
@EnableConfigurationProperties({SdsProperties.class})
@EnableAutoConfiguration
public class SdsAutoConfiguration {

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    @Autowired
    private SdsProperties sdsProperties;

    private volatile SdsClient sdsClient;

    @PostConstruct
    public void init() {
        if (sdsProperties == null || StringUtils.isBlank(sdsProperties.getAppGroupName()) ||
                StringUtils.isBlank(sdsProperties.getAppName()) || StringUtils.isBlank(sdsProperties.
                getServerAddrList())) {
            logger.warn("SdsAutoConfiguration#init 无法通过Spring Boot的Auto Configuration机制来初始化SdsClient。");
            return;
        }

        sdsClient = SdsClientFactory.getOrCreateSdsClient(sdsProperties.getAppGroupName(), sdsProperties.getAppName(),
                sdsProperties.getServerAddrList());
    }

    @Bean
    public SdsClient getOrCreateSdsClient() {
        if (sdsClient == null) {
            throw new IllegalStateException("SdsAutoConfiguration#getOrCreateSdsClient 无法解析到sds.app-group-name、" +
                    "sds.app-name和sds.server-addr-list等properties配置，无法创建SdsClient");
        }

        return sdsClient;
    }
}
