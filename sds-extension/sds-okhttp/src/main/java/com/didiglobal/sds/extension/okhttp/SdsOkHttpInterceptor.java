package com.didiglobal.sds.extension.okhttp;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.contant.ExceptionCode;
import com.didiglobal.sds.client.exception.SdsException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by tianyulei on 2019/8/11
 **/
public class SdsOkHttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        if(StringUtils.isEmpty(url)){
            return chain.proceed(request);
        }
        SdsClient sdsClient = SdsClientFactory.getSdsClient();
        String point = url + "-OkHttpPoint";

        if(sdsClient.shouldDowngrade(point)){
            throw new SdsException(point, ExceptionCode.DOWNGRADE.getCode(), "okhttp request has degraded");
        }
        Throwable exception = null;
        try{
            Response response =  chain.proceed(request);
            return response;
        }catch (Throwable e){
            exception = e;
            throw e;
        } finally {
            if(exception != null){
                sdsClient.exceptionSign(point, exception);
                sdsClient.downgradeFinally(point);
            }
        }

    }
}
