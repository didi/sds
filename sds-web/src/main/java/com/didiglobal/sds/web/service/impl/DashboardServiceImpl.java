package com.didiglobal.sds.web.service.impl;

import com.didiglobal.sds.web.controller.bean.DashboardPoint;
import com.google.common.collect.Lists;

import com.didiglobal.sds.web.dao.HeartbeatDao;
import com.didiglobal.sds.web.dao.bean.HeartbeatDO;
import com.didiglobal.sds.web.service.DashboardService;
import com.didiglobal.sds.web.service.bean.DashboardMainBO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: yizhenqiang
 * @Date: Create in 2019-10-01 11:22
 */
@Service("dashboardService")
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private HeartbeatDao heartbeatDao;

    @Override
    public DashboardMainBO queryDashboardMainShowInfo(String appGroupName, String appName, String point,
                                                      Date startTime, Date endTime) {
        DashboardMainBO result = new DashboardMainBO();
        result.setVisitList(Lists.newArrayList());
        result.setConcurrentList(Lists.newArrayList());
        result.setExceptionList(Lists.newArrayList());
        result.setTimeoutList(Lists.newArrayList());
        result.setDowngradeList(Lists.newArrayList());

        List<HeartbeatDO> heartbeatDOS = heartbeatDao.queryHeartbeatList(appGroupName, appName, point, startTime,
                endTime);
        if (CollectionUtils.isEmpty(heartbeatDOS)) {
            return result;
        }

        heartbeatDOS.stream().forEach(heartbeatDO -> {
            result.getVisitList().add(new DashboardPoint(heartbeatDO.getStatisticsCycleTime().getTime(),
                    heartbeatDO.getVisitNum().doubleValue()));
            result.getConcurrentList().add(new DashboardPoint(heartbeatDO.getStatisticsCycleTime().getTime(),
                    heartbeatDO.getMaxConcurrentNum().doubleValue()));
            result.getExceptionList().add(new DashboardPoint(heartbeatDO.getStatisticsCycleTime().getTime(),
                    heartbeatDO.getExceptionNum().doubleValue()));
            result.getTimeoutList().add(new DashboardPoint(heartbeatDO.getStatisticsCycleTime().getTime(),
                    heartbeatDO.getTimeoutNum().doubleValue()));
            result.getDowngradeList().add(new DashboardPoint(heartbeatDO.getStatisticsCycleTime().getTime(),
                    heartbeatDO.getDowngradeNum().doubleValue()));
        });

        return result;
    }
}
