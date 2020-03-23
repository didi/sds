package com.didiglobal.sds.example.chapter1;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.service.CycleDataService;
import com.didiglobal.sds.client.service.SdsStrategyService;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 样例章节1：通过原生API来使用SDS，不合sds-admin交互
 *
 * @Author: manzhizhen
 * @Date: Create in 2020-03-22 11:02
 */
public class UseOriginApi {

    // 随便写个地址，因为我们此次的演示不会用到sds-admin
    private static final String SERVER_URL = "http://127.0.0.1:8887";

    // 保证SdsClient对象的单例使用(线程安全)
    private static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("BikeBusinessDepartment", "order", SERVER_URL);

    // 创建订单的降级点名称
    private static final String CREATE_ORDER_POINT = "createOrderPoint";

    static {
        // 不从sds-admin获取策略配置，完全使用本地手动设置
        CycleDataService.setPullPointStrategySwitch(false);
        // 也不向sds-admin上报统计数据
        CycleDataService.setUploadDataSwitch(false);

        /**
         * 由于不在sds-admin中配置降级策略，所以我们要本地设置降级策略
         */
        SdsStrategy createOrderSdsStrategy = new SdsStrategy();
        createOrderSdsStrategy.setPoint(CREATE_ORDER_POINT);

        // 使用访问量降级，这里意味着在10s的滑动窗口中最多只能访问1000次业务方法
        createOrderSdsStrategy.setVisitThreshold(1000L);
        // 如果超过设定的阈值，降级比例是100%
        createOrderSdsStrategy.setDowngradeRate(100);
        SdsStrategyService.getInstance().resetOne(CREATE_ORDER_POINT, createOrderSdsStrategy);
    }

    public static void main(String[] args) {

        /**
         * 模拟业务方法的调用者，调用10000次
         * 注意：此时该业务方法的执行受到SDS的包括，在10s的滑动窗口中，最多调用1000次
         */
        int times = 10000;
        while(times-- > 0) {
            Long orderId = createOrder(12345L, "杭州西湖区西溪谷G座");
            System.out.println("新创建的订单id:" + orderId);
        }
    }

    /**
     * 经过SDS包装的某个业务方法：创建订单
     *
     * @param userId
     * @param address
     * @return 创建的订单ID
     */
    public static Long createOrder(Long userId, String address) {
        try {

            // 1. 降级判断：如果需要降级，那我们根据业务逻辑来默认返回null（当然也可以选择抛我们自己定义的业务异常）
            if (sdsClient.shouldDowngrade(CREATE_ORDER_POINT)) {
                return null;
            }

            // 2. 这里写正常的业务逻辑：用控制台输出来代表业务逻辑，为简单起见，返回的订单ID随机生成
            System.out.println("您的业务方法已经执行，userId:" + userId + ", address:" + address);
            return ThreadLocalRandom.current().nextLong(0, 10000000);

        } catch (Exception e) {

            // 3. 这里用于统计异常量
            sdsClient.exceptionSign(CREATE_ORDER_POINT, e);
            // 记得捕获完还得抛出去
            throw e;

        } finally {
            // 4. 回收资源，一般在finally代码中调用
            sdsClient.downgradeFinally(CREATE_ORDER_POINT);
        }
    }
}
