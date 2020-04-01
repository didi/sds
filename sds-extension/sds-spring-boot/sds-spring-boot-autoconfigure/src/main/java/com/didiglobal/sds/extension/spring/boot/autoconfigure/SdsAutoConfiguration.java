package com.didiglobal.sds.extension.spring.boot.autoconfigure;


import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 将SDS和Spring Boot的自动配置打通
 */
@Configuration
public class SdsAutoConfiguration {

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    @Bean
    @ConfigurationProperties("sds")
    public SdsProperties sdsProperties() {
        return new SdsProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public SdsClient sdsClient(SdsProperties sdsProperties) {
        SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient(sdsProperties.getAppGroupName(), sdsProperties.getAppName(),
                sdsProperties.getServerAddrList());
        logger.info("SdsAutoConfiguration create SdsClient with sdsProperties:{}", sdsProperties);
        return sdsClient;
    }
}
