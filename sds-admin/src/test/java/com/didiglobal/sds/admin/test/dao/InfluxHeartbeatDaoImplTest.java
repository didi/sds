package com.didiglobal.sds.admin.test.dao;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.admin.dao.HeartbeatDao;
import com.didiglobal.sds.admin.dao.bean.HeartbeatDO;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: manzhizhen
 * @Date: Create in 2019-09-15 13:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InfluxHeartbeatDaoImplTest {

    @Autowired
    private HeartbeatDao heartbeatDao;

    @Test
    public void addHeartbeatTest() {
        List<HeartbeatDO> heartbeatDOList = Lists.newArrayList();
        HeartbeatDO heartbeatDO = new HeartbeatDO();
        heartbeatDO.setAppGroupName("htw");
        heartbeatDO.setAppName("htw-order");
        heartbeatDO.setPoint("visitPoint");
        heartbeatDO.setDowngradeNum(10L);
        heartbeatDO.setVisitNum(100000L);
        heartbeatDO.setExceptionNum(99L);
        heartbeatDO.setTimeoutNum(23L);
        heartbeatDO.setMaxConcurrentNum(15);
        heartbeatDO.setAppIp("10.2.2.9");
        heartbeatDO.setStatisticsCycleTime(new Date());
        heartbeatDOList.add(heartbeatDO);

        heartbeatDO = new HeartbeatDO();
        heartbeatDO.setAppGroupName("htw");
        heartbeatDO.setAppName("htw-order");
        heartbeatDO.setPoint("visitPoint");
        heartbeatDO.setDowngradeNum(10L);
        heartbeatDO.setVisitNum(100000L);
        heartbeatDO.setExceptionNum(99L);
        heartbeatDO.setTimeoutNum(23L);
        heartbeatDO.setMaxConcurrentNum(15);
        heartbeatDO.setAppIp("10.2.2.9");
        heartbeatDO.setStatisticsCycleTime(new Date());
        heartbeatDOList.add(heartbeatDO);

        heartbeatDO = new HeartbeatDO();
        heartbeatDO.setAppGroupName("htw");
        heartbeatDO.setAppName("htw-order");
        heartbeatDO.setPoint("visitPoint");
        heartbeatDO.setDowngradeNum(10L);
        heartbeatDO.setVisitNum(100000L);
        heartbeatDO.setExceptionNum(99L);
        heartbeatDO.setTimeoutNum(23L);
        heartbeatDO.setMaxConcurrentNum(15);
        heartbeatDO.setAppIp("10.2.2.9");
        heartbeatDO.setStatisticsCycleTime(new Date());
        heartbeatDOList.add(heartbeatDO);

        Assert.assertEquals(heartbeatDOList.size(), heartbeatDao.addHeartbeat(heartbeatDOList));
    }

    @Test
    public void queryHeartbeatListTest() {
        System.out.println(JSON.toJSONString(heartbeatDao.queryHeartbeatList("htw", "htw-order", "visitPoint",
                Date.from(LocalDateTime.now().minusYears(1).atZone(ZoneOffset.ofHours(8)).toInstant()), new Date())));
    }
}
