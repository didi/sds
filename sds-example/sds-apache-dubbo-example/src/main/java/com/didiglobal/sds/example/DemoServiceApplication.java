package com.didiglobal.sds.example;

import com.didiglobal.sds.client.config.SdsDowngradeActionNotify;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
@EnableDubbo
public class DemoServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceApplication.class);

    static {
        SdsDowngradeActionNotify.addDowngradeActionListener((point, downgradeActionType, date) -> {
            logger.info("point:{} is downgraded at {} type:{}", point, date, downgradeActionType);
        });

    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoServiceApplication.class, args);
        System.in.read(); // 按任意键退出
    }


}
