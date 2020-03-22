package com.didiglobal.sds.example.chapter2;

import com.didiglobal.sds.client.exception.SdsException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 通过Spring AOP(Aspectj) + 注解 的方式来使用SDS
 *
 * @Author: manzhizhen
 * @Date: Create in 2020-03-22 21:31
 */
public class UseAnnotationByAspectj {

    public static void main(String[] args) {
        // create and configure beans
        ApplicationContext context = new ClassPathXmlApplicationContext("chapter2-spring.xml");
        OrderManageService orderManageService = (OrderManageService) context.getBean(
                "orderManageService");

        /**
         * 模拟业务方法的调用者，调用10000次
         *
         * 注意：我们已经在演示sds-admin中配置了降级点createOrderPoint的降级策略，此时该业务方法的执行受到SDS的包括，在10s的滑动窗口中，最多调用1000次
         */
        int times = 10000;
        while (times-- > 0) {

            Long orderId = null;
            try {
                orderId = orderManageService.createOrder(12345L, "杭州西湖区西溪谷G座");
                System.out.println("新创建的订单id:" + orderId);

            } catch (SdsException e) {
                System.out.println("此次调用已经被降级！");
            }

        }
    }
}
