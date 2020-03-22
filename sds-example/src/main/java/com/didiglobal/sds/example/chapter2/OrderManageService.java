package com.didiglobal.sds.example.chapter2;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.annotation.SdsDowngradeMethod;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.service.CycleDataService;
import com.didiglobal.sds.client.service.SdsStrategyService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 模拟业务实现类
 *
 * @Author: yizhenqiang
 * @Date: Create in 2020-03-22 22:39
 */
@Service("orderManageService")
public class OrderManageService {

    // 随便写个地址，因为我们此次的演示先不使用sds-admin
    private static final String SERVER_URL = "http://127.0.0.1:8887";

    // 保证SdsClient对象的单例使用(线程安全)
    private static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("两轮车事业部", "order", SERVER_URL);

    // 创建订单的降级点名称
    private final static String CREATE_ORDER_POINT = "createOrderPoint";

    @PostConstruct
    public void init() {
        // 不从sds-admin获取策略配置，完全使用本地手动设置
        CycleDataService.setPullPointStrategySwitch(false);
        // 也不向sds-admin上报统计数据
        CycleDataService.setUploadDataSwitch(false);

        /**
         * 由于我们不使用sds-admin，所以我们得在本地设置降级策略
         */
        SdsStrategy createOrderSdsStrategy = new SdsStrategy();
        createOrderSdsStrategy.setPoint(CREATE_ORDER_POINT);

        // 使用访问量降级，这里意味着在10s的滑动窗口中最多只能访问1000次业务方法
        createOrderSdsStrategy.setVisitThreshold(1000L);
        // 如果超过设定的阈值，降级比例是100%
        createOrderSdsStrategy.setDowngradeRate(100);
        SdsStrategyService.getInstance().resetOne(CREATE_ORDER_POINT, createOrderSdsStrategy);
    }

    /**
     * 经过SDS包装的某个业务方法：创建订单
     *
     * 注意：使用 {@link SdsDowngradeMethod} 默认降级后的行为是抛 {@link com.didiglobal.sds.client.exception.SdsException} 异常
     *
     * @param userId
     * @param address
     * @return 创建的订单ID
     */
    @SdsDowngradeMethod(point = CREATE_ORDER_POINT)
    public Long createOrder(Long userId, String address) {

        // 1. 这里写正常的业务逻辑：用控制台输出来代表业务逻辑，为简单起见，返回的订单ID随机生成
        System.out.println("您的业务方法已经执行，userId:" + userId + ", address:" + address);
        return ThreadLocalRandom.current().nextLong(0, 10000000);

    }
}
