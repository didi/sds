package com.didiglobal.sds.example.chapter4;

import com.didiglobal.sds.client.exception.SdsException;
import com.didiglobal.sds.easy.SdsEasyUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 【演示】Sds和Spring Boot打通
 *
 * 运行Spring Boot的单元测试来验证结果
 *
 * @Author: manzhizhen
 * @Date: Create in 2020-03-29 18:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoootWithSdsTest {

    @Autowired
    private OrderManageService orderManageService;

    @org.junit.Test
    public void test() {
        int times = 2000;
        while (times-- > 0) {
            Long orderId = null;
            try {
                orderId = orderManageService.createOrder(12345L, "杭州西湖区西溪谷G座");
                System.out.println("createOrder新创建的订单id:" + orderId);

            } catch (SdsException e) {
                System.out.println("createOrder此次调用已经被降级！");
            }
        }


        times = 2000;
        while (times-- > 0) {
            Long orderId = null;
            try {

                orderId = SdsEasyUtil.invokerMethod(OrderManageService.CREATE_ORDER_POINT_1, null,
                        ()-> orderManageService.createOrder1(12345L, "杭州西湖区西溪谷G座"));

                System.out.println("createOrder1新创建的订单id:" + orderId);

            } catch (SdsException e) {
                System.out.println("createOrder1此次调用已经被降级！");
            }
        }
    }
}
