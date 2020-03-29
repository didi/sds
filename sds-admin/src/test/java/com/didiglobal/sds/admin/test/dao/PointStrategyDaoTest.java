package com.didiglobal.sds.admin.test.dao;

import com.didiglobal.sds.admin.dao.PointStrategyDao;
import com.didiglobal.sds.admin.dao.bean.PointStrategyDO;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by manzhizhen on 18/2/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PointStrategyDaoTest {

    @Autowired
    private PointStrategyDao pointStrategyDao;

    @Test
    public void addStrategyTest() {
        PointStrategyDO strategyDO = new PointStrategyDO();
        strategyDO.setAppGroupName("BikeBusinessDepartment");
        strategyDO.setAppName("bh-order");
        strategyDO.setPoint("testPoint22");
        strategyDO.setSdsSchemeName("FIRST_GROUP");
        strategyDO.setVisitThreshold(50000L);
        strategyDO.setConcurrentThreshold(20);
        strategyDO.setExceptionThreshold(100L);
        strategyDO.setExceptionRateThreshold(50);
        strategyDO.setExceptionRateStart(10000L);
        strategyDO.setTimeoutThreshold(30L);
        strategyDO.setTimeoutCountThreshold(10000L);
        strategyDO.setDelayTime(60L);
        strategyDO.setRetryInterval(10L);
        strategyDO.setDowngradeRate(100);
        strategyDO.setStatus(1);
        strategyDO.setCreatorName("路飞");
        strategyDO.setCreatorEmail("manzhizhen@didichuxing.com");
        strategyDO.setOperatorName("路飞");
        strategyDO.setOperatorEmail("manzhizhen@didichuxing.com");

        System.out.println(pointStrategyDao.addPointStrategy(strategyDO));

        strategyDO = new PointStrategyDO();
        strategyDO.setAppGroupName("BikeBusinessDepartment");
        strategyDO.setAppName("bh-order");
        strategyDO.setPoint("testPoint23");
        strategyDO.setSdsSchemeName("FIRST_GROUP");
        strategyDO.setDowngradeRate(100);
        strategyDO.setStatus(1);

        strategyDO.setCreatorEmail("manzhizhen@didiglobal.com");
        strategyDO.setCreatorName("路飞");
        strategyDO.setOperatorEmail("manzhizhen@didiglobal.com");
        strategyDO.setOperatorName("路飞");

        System.out.println(pointStrategyDao.addPointStrategy(strategyDO));

    }

    @Test
    public void updatePointStrategyUpdate() {
        PointStrategyDO pointStrategyDO = new PointStrategyDO();

        pointStrategyDO.setAppGroupName("hm");
        pointStrategyDO.setAppName("bh-marketing-activity");
        pointStrategyDO.setPoint("groupBuyQueryCardConfig");
        pointStrategyDO.setSdsSchemeName("QPS自动降级");
        pointStrategyDO.setNewSdsSchemeName("QPS自动降级");
        pointStrategyDO.setStatus(1);
        pointStrategyDO.setDowngradeRate(100);
        pointStrategyDO.setCreatorName("路飞");
        pointStrategyDO.setCreatorEmail("manzhizhen@didichuxing.com");
        pointStrategyDO.setOperatorName("路飞");
        pointStrategyDO.setOperatorEmail("manzhizhen@didichuxing.com");

        System.out.println(pointStrategyDao.updatePointStrategy(pointStrategyDO));
    }

    @Test
    public void queryPointStrategyTest() {
        System.out.println(pointStrategyDao.queryPointStrategyBatch("BikeBusinessDepartment", "order", Lists.newArrayList("testPoint"),
                "FIRST_GROUP"));
        System.out.println(
                pointStrategyDao.queryPointStrategyBatch("BikeBusinessDepartment", "order", Lists.newArrayList("abcdefPoint"),
                        "FIRST_GROUP"));
    }

    @Test
    public void queryPointStrategyByPageTest() {
        System.out.println(pointStrategyDao.queryPointStrategyByPage("BikeBusinessDepartment", "order", "testPoint", null, 0, 10));
    }
}
