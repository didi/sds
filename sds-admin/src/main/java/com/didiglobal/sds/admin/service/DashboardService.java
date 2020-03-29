package com.didiglobal.sds.admin.service;

import com.didiglobal.sds.admin.service.bean.DashboardMainBO;

import java.util.Date;

/**
 * @Description: 降级仪表盘服务接口
 * @Author: manzhizhen
 * @Date: Create in 2019-10-01 11:16
 */
public interface DashboardService {

    /**
     * 通过时间范围查询某个降级点的展示信息
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @param tartTime
     * @param endTime
     * @return
     */
    DashboardMainBO queryDashboardMainShowInfo(String appGroupName, String appName, String point, Date tartTime,
                                               Date endTime);
}
