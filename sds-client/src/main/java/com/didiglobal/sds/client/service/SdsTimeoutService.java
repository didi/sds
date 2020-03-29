package com.didiglobal.sds.client.service;

/**
 * Created by manzhizhen on 2016/6/22.
 */
public class SdsTimeoutService {

    private static SdsTimeoutService sdsTimeoutService = new SdsTimeoutService();

    private SdsTimeoutService() {}

    public static SdsTimeoutService getInstance() {
        return sdsTimeoutService;
    }




}
