package com.didiglobal.sds.example.chapter4;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.annotation.SdsDowngradeMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 模拟业务实现类
 *
 * 这里演示两种方式使用SDS，方式1：Spring AOP + 注解 的方式； 方式2：使用 SdsEasyUtil 或 SdsClient
 *
 * @Author: manzhizhen
 * @Date: Create in 2020-03-22 22:39
 */
@Service("orderManageService")
public class OrderManageService {

    // 创建订单的降级点名称
    public final static String CREATE_ORDER_POINT = "createOrderPoint";
    public final static String CREATE_ORDER_POINT_1 = "createOrderPoint1";

    // 如果直接使用SDS API，那么可以使用Spring来注入SdsClient
    @Autowired
    private SdsClient sdsClient;

    /**
     * 方式1：Spring AOP + 注解 的方式
     *
     * 创建订单
     *
     * 注意：使用 {@link SdsDowngradeMethod} 默认降级后的行为是抛 {@link com.didiglobal.sds.client.exception.SdsException} 异常
     *
     * @param userId
     * @param address
     * @return 创建的订单ID
     */
    @SdsDowngradeMethod(point = CREATE_ORDER_POINT)
    public Long createOrder(Long userId, String address) {

        // 1. 这里是正常的业务逻辑：用控制台输出来代表业务逻辑，为简单起见，返回的订单ID随机生成
        System.out.println("您的业务方法已经执行，userId:" + userId + ", address:" + address);
        return ThreadLocalRandom.current().nextLong(0, 10000000);
    }

    /**
     * 方式2：a-在调用方使用{@link com.didiglobal.sds.easy.SdsEasyUtil}来调用该方法 或者
     *  b-直接使用sdsClient来包createOrder1,
     *  这里就不在演示了，参见{@link com.didiglobal.sds.example.chapter1.UseOriginApi#createOrder(Long, String)}）
     *
     *  创建订单1
     *
     * @param userId
     * @param address
     * @return 创建的订单ID
     */
    public Long createOrder1(Long userId, String address) {

        // 1. 这里是正常的业务逻辑：用控制台输出来代表业务逻辑，为简单起见，返回的订单ID随机生成
        System.out.println("您的业务方法已经执行，userId:" + userId + ", address:" + address);
        return ThreadLocalRandom.current().nextLong(0, 10000000);
    }
}
