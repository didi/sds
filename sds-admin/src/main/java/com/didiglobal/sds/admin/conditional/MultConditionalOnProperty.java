package com.didiglobal.sds.admin.conditional;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 相比ConditionalOnProperty支持多个条件的判断
 * @Author: manzhizhen
 * @Date: Create in 2019-09-15 13:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(MultOnPropertyCondition.class)
public @interface MultConditionalOnProperty {

    String value() default "";

    String name() default "";

    String[] havingValue() default {};
}
