package com.didiglobal.sds.client;

import com.didiglobal.sds.client.exception.SdsException;

import java.lang.reflect.Method;

/**
 * <p>description : SdsCallBack
 *
 * @author : masteryourself
 * @version : 1.0.0
 * @date : 2020/3/21 17:19
 */
public interface SdsExceptionCallBack {

    /**
     * 异常之后的回调方法
     *
     * @param method     方法
     * @param parameters 入参
     * @param e          异常信息
     * @return 返回值，需要与原方法的返回值保持一致, 否则处理后会抛出类型转换异常, 建议按照接口统一返回值编程
     */
    Object handle(Method method, Object[] parameters, SdsException e);

}
