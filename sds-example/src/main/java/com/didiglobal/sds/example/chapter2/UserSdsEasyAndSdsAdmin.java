package com.didiglobal.sds.example.chapter2;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.easy.SdsEasyUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 【演示】使用sds-easy中提供的Lamdba表达式的方式来使用sds-client
 *
 * @Author: manzhizhen
 * @Date: Create in 2020-03-27 09:04
 */
public class UserSdsEasyAndSdsAdmin {

    // 这里配置我们官方的远程演示sds-admin地址
    private static final String SERVER_URL = "https://sds.chpengzh.com/";

    // 保证SdsClient对象的单例使用(线程安全)
    private static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("BikeBusinessDepartment", "order",
            SERVER_URL);

    // 创建订单的降级点名称
    private static final String CREATE_ORDER_POINT = "createOrderPoint";

    public static void main(String[] args) {

        /**
         * 模拟业务方法的调用者，调用2000次
         * 注意：我们已经在演示sds-admin中配置了降级点createOrderPoint的降级策略，此时该业务方法的执行受到SDS的保护，在10s的滑动窗口中，最多调用1000次
         */
        int times = 2000;
        while (times-- > 0) {

            /**
             * 使用了sds-easy的 {@link SdsEasyUtil} 后，再也不用在业务方法中进行SDS的埋点了，多方便啊！！！
             * 注意：我们这里设置了降级后默认的返回值是null
             */
            Long orderId = SdsEasyUtil.invokerMethod(CREATE_ORDER_POINT, null,
                    () -> createOrder(12345L, "杭州西湖区西溪谷G座")
            );

            System.out.println("新创建的订单id:" + orderId);
        }

    }

    /**
     * 某个业务方法：创建订单
     *
     * 注意：因为我们打算使用{@link SdsEasyUtil}，所以这里业务方法不需要进行sds-client的埋点
     *
     * @param userId
     * @param address
     * @return 创建的订单ID
     */
    public static Long createOrder(Long userId, String address) {

        // 1. 这里是正常的业务逻辑：用控制台输出来代表业务逻辑，为简单起见，返回的订单ID随机生成
        System.out.println("您的业务方法已经执行，userId:" + userId + ", address:" + address);
        return ThreadLocalRandom.current().nextLong(0, 10000000);
    }
}
