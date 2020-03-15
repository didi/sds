package com.didiglobal.sds.easy.test;

import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.easy.SdsEasyUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SdsEasyUtilTest {

    protected static final String SERVER_URL = "http://localhost:8887";

    static {
        // 可以找个安静的地方初始化SdsClient
        SdsClientFactory.getOrCreateSdsClient("hm", "bh-order", SERVER_URL);
    }

    // 这里假装是业务Service
    private ThreadLocal<String> traceIdService = ThreadLocal.withInitial(() -> "古墓丽影");

    @Test
    public void invokerMethodTest() {

        // 某个局部变量（路人甲）
        Date date = new Date();

        /**
         * 包含降级判断的业务逻辑执行
         *
         * 注意：SdsEasyUtil类是简化神器
         */
        String result = SdsEasyUtil.invokerMethod("somePoint", "我是降级后的默认值", () -> {

            // 这里可以添加一些奇怪的业务逻辑
            System.out.println(date);
            return traceIdService.get();

        });
        Assert.assertEquals("古墓丽影", result);

        try {
            result = SdsEasyUtil.invokerMethod("somePoint", "我是降级后的默认值", () -> {

                if (System.currentTimeMillis() > 1000) {
                    throw new IllegalStateException("必抛异常");
                }

                return traceIdService.get();
            });

            Assert.fail();

        } catch (Throwable e) {
            Assert.assertNotNull(e);
        }
    }


    @Test
    public void invokerMethodWithoutReturnTest() {
        int i = 100;
        while (i-- > 0) {
            SdsEasyUtil.invokerMethodWithoutReturn("testPoint", () -> {
                System.out.println("哈哈");
            });

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void invokerMethodWithDowngradeFunctionTest() {

        // 某个局部变量（路人甲）
        Date date = new Date();

        /**
         * 包含降级判断的业务逻辑执行
         *
         * 注意：SdsEasyUtil类是简化神器
         */
        String result = SdsEasyUtil.invokerMethodWithDowngradeFunction("somePoint",
                () -> {
                    // 这里可以添加一些奇怪的降级后的其他操作
                    System.out.println("心累");
                    return "我是降级后的默认值，但我需要做些其他工作再返回";
                },
                () -> {

                    // 这里可以添加一些奇怪的业务逻辑
                    System.out.println(date);
                    return traceIdService.get();

                }
        );

        Assert.assertEquals("古墓丽影", result);
    }


    @Test
    public void oneButtonFuseSwitchTest() {
        Assert.assertEquals("古墓丽影", bizMethod());
    }

    /**
     * 某可读性超强的业务方法
     *
     * @return
     */
    private String bizMethod() {

        /**
         * 降级点somePoint用来做一键熔断开关
         */
        if (SdsEasyUtil.oneButtonFuseSwitch("somePoint")) {
            return "我是降级后的默认值";
        }

        // 正常业务逻辑
        return traceIdService.get();
    }
}
