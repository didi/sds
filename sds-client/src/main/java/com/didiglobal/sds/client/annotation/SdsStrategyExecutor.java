/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.annotation;

import java.lang.annotation.*;

/**
 * 服务降级方法注解
 * 
 * 
 * @author  manzhizhen
 * @version $Id: SdsDowngradeMethod.java, v 0.1 2016年1月9日 上午10:43:45 Administrator Exp $
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SdsStrategyExecutor {

    /**
     * 策略执行顺序，取值0~Integer.MAX_VALUE，越小就越先执行
     */
    int sort();
}
