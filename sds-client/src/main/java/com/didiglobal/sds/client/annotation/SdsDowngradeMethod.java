/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.annotation;

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
     * 异常类型
     */
    Class<?> exceptionClass() default Exception.class;

    /**
     * 出现异常后的降级方法名称，和使用注解标注的方法在同一个类中，注意这里有降级的优先级顺序
     * fallback 指定的方法 > sds-admin 配置的策略 > 默认（抛出异常）
     *
     * @return 方法名称
     */
    String fallback() default "";
}
