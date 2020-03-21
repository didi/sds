/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.annotation;

import com.didiglobal.sds.client.DefaultSdsExceptionCallBack;
import com.didiglobal.sds.client.SdsExceptionCallBack;
import com.didiglobal.sds.client.exception.SdsException;

import java.lang.annotation.*;

/**
 * 服务降级方法注解
 *
 * @author  manzhizhen
 * @version $Id: SdsDowngradeMethod.java, v 0.1 2016年1月9日 上午10:43:45 Administrator Exp $
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SdsDowngradeMethod {

    /**
     * 降级点
     */
    String point();

    /**
     * 需要处理的异常, 希望以后的异常能够遵守规范, 继承自 SdsException（或者制定一个 baseException, 以后限流的异常都使用它）, 否则这里以后可能需要改动
     *
     * @return {@link SdsException}
     */
    Class<? extends SdsException>[] includeExceptions() default SdsException.class;

    /**
     * 出异常之后的回调方法, 不建议使用默认的, 默认的还是抛出异常
     *
     * @return {@link SdsExceptionCallBack}
     */
    Class<? extends SdsExceptionCallBack> callback() default DefaultSdsExceptionCallBack.class;
}
