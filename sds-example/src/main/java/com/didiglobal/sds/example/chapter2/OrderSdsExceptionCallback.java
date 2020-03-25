package com.didiglobal.sds.example.chapter2;

import com.didiglobal.sds.client.SdsExceptionCallBack;
import com.didiglobal.sds.client.exception.SdsException;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <p>description : OrderSdsExceptionCallback
 *
 * @author : masteryourself
 * @version : 1.0.1
 * @date : 2020/3/25 10:04
 */
public class OrderSdsExceptionCallback implements SdsExceptionCallBack {

    private static final String CREATE_ORDER = "createOrder";

    @Override
    public Object handle(Method method, Object[] parameters, SdsException e) {
        if (method.getName().equals(CREATE_ORDER)) {
            System.out.printf("%s 方法降级，入参是 %s \n", CREATE_ORDER, Arrays.toString(parameters));
            return BaseResult.error("方法降级");
        }
        throw e;
    }

}
