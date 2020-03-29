/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.service;

import com.alibaba.fastjson.JSONObject;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.StringUtils;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 降级返回值服务类
 * <p>
 * 结合AOP使用， {@link{SdsDowngradeAspectService}}
 *
 * @author manzhizhen
 * @version $Id: SdsDowngradeReturnValueService.java, v 0.1 2016年2月7日 下午12:33:15 Administrator Exp $
 */
final public class SdsDowngradeReturnValueService {

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    private ConcurrentHashMap<String, ReturnValue> pointDowngradeReturnValue = new ConcurrentHashMap<>();

    private final static SdsDowngradeReturnValueService downgradeReturnValueService =
            new SdsDowngradeReturnValueService();

    private SdsDowngradeReturnValueService() {
    }

    public static SdsDowngradeReturnValueService getInstance() {
        return downgradeReturnValueService;
    }

    /**
     * 通过降级点获取返回值
     * 注意：如果发生如下两种情况，那么将返回null
     * 1. 如果该降级点在服务端没有配置
     * 2. json转化发生异常
     *
     * @param point 降级点名称
     * @param type  json串转化的对象类型
     * @return
     */
    public <T> T getDowngradeReturnValue(String point, Type type) {
        if (point == null) {
            return null;
        }

        ReturnValue returnValue = pointDowngradeReturnValue.get(point);
        if (returnValue == null || StringUtils.isBlank(returnValue.getReturnValueStr()) || type == null) {
            return null;
        }


        try {
            return JSONObject.parseObject(returnValue.getReturnValueStr(), type);

        } catch (Exception e) {
            logger.error("SdsDowngradeReturnValueService#getDowngradeReturnValue json string parse object " +
                    "has exception, json:" + returnValue.getReturnValueStr() + ", type:" + type, e);
        }

        return null;
    }

    /**
     * 重设降级点返回值
     *
     * @param strategyMap
     */
    public void reset(ConcurrentHashMap<String, SdsStrategy> strategyMap) {

        ConcurrentHashMap<String, ReturnValue> newMap = new ConcurrentHashMap<>();

        if (strategyMap == null || strategyMap.isEmpty()) {
            this.pointDowngradeReturnValue = newMap;
            return;
        }

        for (Map.Entry<String, SdsStrategy> stringSdsStrategyEntry : strategyMap.entrySet()) {
            newMap.put(stringSdsStrategyEntry.getKey(),
                    new ReturnValue(stringSdsStrategyEntry.getKey(), stringSdsStrategyEntry.getValue().
                            getReturnValueStr(), null));
        }

        this.pointDowngradeReturnValue = newMap;
    }

    /**
     * 返回值类
     */
    class ReturnValue {

        private String point;

        private String returnValueStr;

        private Object returnValue;

        public ReturnValue() {
        }

        public ReturnValue(String point, String returnValueStr, Object returnValue) {
            this.point = point;
            this.returnValueStr = returnValueStr;
            this.returnValue = returnValue;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getReturnValueStr() {
            return returnValueStr;
        }

        public void setReturnValueStr(String returnValueStr) {
            this.returnValueStr = returnValueStr;
        }

        public Object getReturnValue() {
            return returnValue;
        }

        public void setReturnValue(Object returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ReturnValue{");
            sb.append("point='").append(point).append('\'');
            sb.append(", returnValueStr='").append(returnValueStr).append('\'');
            sb.append(", returnValue=").append(returnValue);
            sb.append('}');
            return sb.toString();
        }
    }
}
