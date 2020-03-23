package com.didiglobal.sds.client;


import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.AssertUtil;
import com.didiglobal.sds.client.util.StringUtils;
import org.slf4j.Logger;

import static com.didiglobal.sds.client.contant.BizConstant.APP_GROUP_NAME;
import static com.didiglobal.sds.client.contant.BizConstant.APP_NAME;
import static com.didiglobal.sds.client.contant.BizConstant.SERVER_ADDR_LIST;

/**
 * SdsClient单例工厂
 * <p>
 * Created by manzhizhen on 17/1/1.
 */
public final class SdsClientFactory {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    /**
     * 单例
     */
    private volatile static AbstractSdsClient instance = null;

    static {
        String appGroupName = System.getProperty(APP_GROUP_NAME);
        String appName = System.getProperty(APP_NAME);
        String serverAddrList = System.getProperty(SERVER_ADDR_LIST);

        if (StringUtils.isBlank(appGroupName) || StringUtils.isBlank(appName) || StringUtils.isBlank(serverAddrList)) {
            logger.info("SdsClientFactory#static 系统参数" + APP_NAME + ", " + APP_NAME + ", " + SERVER_ADDR_LIST +
                    "没配置全，无法直接初始化SdsClient。");

        } else {
            instance = buildSdsClient(appGroupName, appName, serverAddrList);
        }
    }

    /**
     * 获取或创建一个SdsClient实例
     *
     * @param appGroupName   应用组名称
     * @param appName        应用名称
     * @param serverAddrList Sds服务端地址列表，可以填写多个服务端地址，为了HA和Load Balance，多个地址用英文逗号分隔
     * @return
     */
    public static SdsClient getOrCreateSdsClient(String appGroupName, String appName, String serverAddrList) {

        if (instance != null) {
            logger.warn("SdsClientFactory#getOrCreateSdsClient 创建SdsClient实例失败，因为单例SdsClient已经存在，" +
                    "appGroupName：" + instance.getAppGroupName() + ", appName:" + instance.getAppName() + ",  " +
                    "serverAddrList:" + instance.getServerAddrList());

            return instance;
        }

        synchronized (SdsClientFactory.class) {
            if (instance != null) {
                logger.warn("SdsClientFactory#getOrCreateSdsClient 创建SdsClient实例失败，因为单例SdsClient已经存在，" +
                        "appGroupName：" + instance.getAppGroupName() + ", appName:" + instance.getAppName() + ",  " +
                        "serverAddrList:" + instance.getServerAddrList());
                return instance;
            }

            instance = buildSdsClient(appGroupName, appName, serverAddrList);
        }

        return instance;
    }

    /**
     * 获取当前的SdsClient实例
     *
     * @return
     */
    public static SdsClient getSdsClient() {
        return instance;
    }

    private static AbstractSdsClient buildSdsClient(String appGroupName, String appName, String serverAddrList) {
        AssertUtil.notBlack(appGroupName, "appGroupName can not black!");
        AssertUtil.notBlack(appName, "appName can not black!");
        AssertUtil.notBlack(serverAddrList, "serverAddrList can not black!");

        // 默认返回CommonSdsClient
        AbstractSdsClient sdsClient = new CommonSdsClient(appGroupName, appName, serverAddrList);
        logger.info("SdsClientFactory#buildSdsClient 创建SdsClient成功，sdsClient：" + sdsClient);

        return sdsClient;
    }

}
