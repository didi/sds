package com.didiglobal.sds.example.chapter2;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.annotation.SdsDowngradeMethod;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 模拟业务实现类
 *
 * @Author: yizhenqiang
 * @Date: Create in 2020-03-22 22:39
 */
@Service("orderManageService")
public class OrderManageService {

    // 这里配置我们官方的远程演示sds-admin地址
    private static final String SERVER_URL = "https://sds.chpengzh.com/";

    // 保证SdsClient对象的单例使用(线程安全)
    private static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("BikeBusinessDepartment", "order", SERVER_URL);

    // 创建订单的降级点名称
    private final static String CREATE_ORDER_POINT = "createOrderPoint";

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

        // 1. 这里是正常的业务逻辑：用控制台输出来代表业务逻辑，为简单起见，返回的订单ID随机生成
        System.out.println("您的业务方法已经执行，userId:" + userId + ", address:" + address);
        return ThreadLocalRandom.current().nextLong(0, 10000000);

    }
}
