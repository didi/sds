package com.didiglobal.sds.client;

import com.didiglobal.sds.client.exception.SdsException;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.Method;

/**
 * <p>description : DefaultSdsCallBack, 这个类不做任何业务处理, 只打印日志
 *
 * @author : masteryourself
 * @version : 1.0.0
 * @date : 2020/3/21 17:23
 */
public class DefaultSdsExceptionCallBack implements SdsExceptionCallBack {

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    @Override
    public Object handle(Method method, Object[] parameters, SdsException e) {
        logger.info("method {}, parameters {}  occur {}", method.getName(), parameters, e.getMessage());
        throw e;
    }

}
