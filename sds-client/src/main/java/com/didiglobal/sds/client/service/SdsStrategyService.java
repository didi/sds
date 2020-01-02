/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.service;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略服务
 *
 * @author yizhenqiang
 * @version $Id: SdsStrategyService.java, v 0.1 2016年1月12日 上午12:03:55 Administrator Exp $
 */
final public class SdsStrategyService {

    private final static SdsStrategyService strategyService = new SdsStrategyService();

    // key:降级点, value:策略
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
     * 更新策略信息
     *
     * @param strategyMap
     */
    public void reset(ConcurrentHashMap<String, SdsStrategy> strategyMap) {
        if (strategyMap != null) {
            Map<String, SdsStrategy> oldMap = this.strategyMap;
            this.strategyMap = strategyMap;

            logger.info("SdsStrategyService 策略更新，旧：" + JSON.toJSONString(oldMap) + ", 新："
                    + JSON.toJSONString(this.strategyMap));

            // 重设降级延长
            SdsDowngradeDelayService.getInstance().addOrUpdatePointDelay(strategyMap);

            // 重设并发阈值
            SdsPowerfulCounterService.getInstance().updateConcurrent();

        }
    }

    public ConcurrentHashMap<String, SdsStrategy> getStrategyMap() {
        return strategyMap;
    }
}
