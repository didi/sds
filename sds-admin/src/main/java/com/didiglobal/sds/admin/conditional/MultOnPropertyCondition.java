package com.didiglobal.sds.admin.conditional;

import com.didiglobal.sds.client.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @Description:
 * @Author: manzhizhen
 * @Date: Create in 2019-09-15 13:45
 */
public class MultOnPropertyCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(MultConditionalOnProperty.
                class.getName());
        String propertyName = (String) annotationAttributes.get("name");
        if (StringUtils.isBlank(propertyName)) {
            propertyName = (String) annotationAttributes.get("value");
        }

        if (StringUtils.isBlank(propertyName)) {
            return new ConditionOutcome(false, "没发现配置name或value");
        }

        String[] values = (String[]) annotationAttributes.get("havingValue");
        if (values.length == 0) {
            return new ConditionOutcome(false, "没发现配置havingValue");
        }

        String propertyValue = context.getEnvironment().getProperty(propertyName);
        if (StringUtils.isBlank(propertyValue)) {
            propertyValue = ((ConfigurableApplicationContext) context.getResourceLoader()).getEnvironment().
                    getProperty(propertyName);

            if (StringUtils.isBlank(propertyValue)) {
                return new ConditionOutcome(false, "没发现配置" + propertyName);
            }
        }

        /**
         * 相当于或的关系，只要有一个能匹配就算成功
         */
        for (String havingValue : values) {
            if (propertyValue.equalsIgnoreCase(havingValue)) {
                return new ConditionOutcome(true, "匹配成功");
            }
        }

        return new ConditionOutcome(false, "匹配失败");
    }
}
