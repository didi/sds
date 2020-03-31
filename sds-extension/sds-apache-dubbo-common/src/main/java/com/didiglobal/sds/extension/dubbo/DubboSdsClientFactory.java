package com.didiglobal.sds.extension.dubbo;

import com.didiglobal.sds.client.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.didiglobal.sds.client.contant.BizConstant.*;

/**
 * Created by sea on 2020-03-29.
 */
public enum DubboSdsClientFactory {
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(DubboSdsClientFactory.class);

    private final DubboSdsClient dubboSdsClient;

    DubboSdsClientFactory() {
        String appGroupName = System.getProperty(APP_GROUP_NAME);
        String appName = System.getProperty(APP_NAME);
        String serverAddrList = System.getProperty(SERVER_ADDR_LIST);
        if (StringUtils.isBlank(appGroupName) || StringUtils.isBlank(appName) || StringUtils.isBlank(serverAddrList)) {
            logger.info("系统参数{}, {}, {}没配置全，无法直接初始化SdsClient。", APP_NAME, APP_NAME, SERVER_ADDR_LIST);
            this.dubboSdsClient = null;
        } else {
            this.dubboSdsClient = new DubboSdsClient(appGroupName, appName, serverAddrList);
        }

    }

    public DubboSdsClient getDubboSdsClient() {
        return dubboSdsClient;
    }
}
