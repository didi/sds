package com.didiglobal.sds.admin.service;

import com.didiglobal.sds.client.bean.HeartBeatResponse;
import com.didiglobal.sds.client.bean.HeartbeatRequest;
import com.didiglobal.sds.admin.service.bean.HeartbeatShowBO;

import java.util.Date;

/**
 * Created by manzhizhen on 18/2/12.
 */
public interface HeartbeatService {

    /**
     * 保存心跳数据
     *
     * @param heartbeatRequest
     * @return
     */
    HeartBeatResponse saveHeartbeatInfo(HeartbeatRequest heartbeatRequest);

    /**
     * 检查并拉取最新的降级点策略配置
     *
     * @param heartbeatRequest
     * @return
     */
    HeartBeatResponse checkAndGetNewestPointStrategy(HeartbeatRequest heartbeatRequest);

    /**
     * 按条件查询心跳数据
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param startTime
     * @param endTime
     * @return
     */
    HeartbeatShowBO queryHeartbeatList(String appGroupName, String appName, String point, Date startTime, Date endTime);
}
