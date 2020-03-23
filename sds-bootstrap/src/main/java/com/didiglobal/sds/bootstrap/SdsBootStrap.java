package com.didiglobal.sds.bootstrap;

import com.didiglobal.sds.bootstrap.transformer.SdsClassFileTransformer;
import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.lang.instrument.Instrumentation;

/**
 * Created by manzhizhen on 17/3/26.
 */
public class SdsBootStrap {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    private volatile static SdsClient client = null;

    private static final int BOOTSTRAP_PARAM_NUM = 4;

    /**
     * VM Options: -javaagent:/Users/manzhizhen/Documents/git/daijia-sds/sds-bootstrap/target/sds-bootstrap.jar=heima,
     * mzz-study,url,com.manzhizhen
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {

        if (agentArgs == null) {
            agentArgs = "";
        }

        logger.info("Sds agentArgs:" + agentArgs);

        String[] param = agentArgs.split(",");

        /**
         * 参数1：应用组名称
         * 参数2：应用名称
         * 参数3：sds服务端url
         * 参数4：需要sds扫描的包路径
         */
        if (param.length != BOOTSTRAP_PARAM_NUM) {
            logger.error("Sds agentArgs num not 4:" + agentArgs);
            return;
        }

        client = SdsClientFactory.getOrCreateSdsClient(param[0], param[1], param[2]);

        logger.info("sdsClient: " + client);

        instrumentation.addTransformer(new SdsClassFileTransformer(param[3]));

    }

    public static SdsClient getClient() {
        return client;
    }

    private static String getClassPathFromSystemProperty() {
        return System.getProperty("java.class.path");
    }
}
