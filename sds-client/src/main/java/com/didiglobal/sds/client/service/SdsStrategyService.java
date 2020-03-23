/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.service;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.StringUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 降级策略服务管理
 *
 * @author manzhizhen
 * @version $Id: SdsStrategyService.java, v 0.1 2016年1月12日 上午12:03:55 Administrator Exp $
 */
final public class SdsStrategyService {

    private final static SdsStrategyService strategyService = new SdsStrategyService();

    /**
     * 保存所有的降级点和降级策略的映射关系
     * key:降级点, value:策略
     */
    private ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    public static SdsStrategyService getInstance() {
        return strategyService;
    }

    private SdsStrategyService() {
    }

    /**
     * 通过降级点获取策略
     *
     * @param point
     * @return
     */
    public SdsStrategy getStrategy(String point) {
        return strategyMap.get(point);
    }

    /**
     * 更新某个降级策略信息
     *
     * @param point
     * @param sdsStrategy
     * @return true-更新成功，false-更新失败
     */
    public boolean resetOne(String point, SdsStrategy sdsStrategy) {
        if (StringUtils.isNotBlank(point)) {
            SdsStrategy oldSdsStrategy = this.strategyMap.get(point);
            this.strategyMap.put(point, sdsStrategy);

            logger.info("SdsStrategyService 单个策略更新，旧：" + oldSdsStrategy + ", 新：" + sdsStrategy);

            // 重设降级延长
            SdsDowngradeDelayService.getInstance().addOrUpdatePointDelay(strategyMap);

            // 重设并发阈值
            SdsPowerfulCounterService.getInstance().updateConcurrent();

            return true;
        }

        return false;
    }

    /**
     * 更新所有的降级策略信息
     *
     * @param strategyMap
     * @return true-更新成功，false-更新失败
     */
    public boolean resetAll(ConcurrentHashMap<String, SdsStrategy> strategyMap) {
        if (strategyMap != null) {
            Map<String, SdsStrategy> oldMap = this.strategyMap;
            this.strategyMap = strategyMap;

            logger.info("SdsStrategyService 策略更新，旧：" + JSON.toJSONString(oldMap) + ", 新："
                    + JSON.toJSONString(this.strategyMap));

            // 重设降级延长
            SdsDowngradeDelayService.getInstance().addOrUpdatePointDelay(strategyMap);

            // 重设并发阈值
            SdsPowerfulCounterService.getInstance().updateConcurrent();

            return true;
        }

        return false;
    }

    /**
     * 获取所有的降级策略信息
     *
     * @return
     */
    public ConcurrentHashMap<String, SdsStrategy> getStrategyMap() {
        return strategyMap;
    }
}
