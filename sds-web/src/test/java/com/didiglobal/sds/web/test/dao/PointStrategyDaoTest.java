package com.didiglobal.sds.web.test.dao;

import com.didiglobal.sds.web.dao.PointStrategyDao;
import com.didiglobal.sds.web.dao.bean.PointStrategyDO;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yizhenqiang on 18/2/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PointStrategyDaoTest {

    @Autowired
    private PointStrategyDao pointStrategyDao;

    @Test
    public void addStrategyTest() {
        PointStrategyDO strategyDO = new PointStrategyDO();
        strategyDO.setAppGroupName("黑马");
        strategyDO.setAppName("bh-order");
        strategyDO.setPoint("testPoint22");
        strategyDO.setStrategyGroupName("FIRST_GROUP");
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
        strategyDO.setCreatorName("易振强");
        strategyDO.setCreatorEmail("yizhenqiang@didichuxing.com");
        strategyDO.setOperatorName("易振强");
        strategyDO.setOperatorEmail("yizhenqiang@didichuxing.com");

        System.out.println(pointStrategyDao.addPointStrategy(strategyDO));

        strategyDO = new PointStrategyDO();
        strategyDO.setAppGroupName("黑马");
        strategyDO.setAppName("bh-order");
        strategyDO.setPoint("testPoint23");
        strategyDO.setStrategyGroupName("FIRST_GROUP");
        strategyDO.setDowngradeRate(100);
        strategyDO.setStatus(1);
        System.out.println(pointStrategyDao.addPointStrategy(strategyDO));

    }

    @Test
    public void updatePointStrategyUpdate() {
        PointStrategyDO pointStrategyDO = new PointStrategyDO();

        pointStrategyDO.setAppGroupName("hm");
        pointStrategyDO.setAppName("bh-marketing-activity");
        pointStrategyDO.setPoint("groupBuyQueryCardConfig");
        pointStrategyDO.setStrategyGroupName("QPS自动降级");
        pointStrategyDO.setNewStrategyGroupName("QPS自动降级");
        pointStrategyDO.setStatus(1);
        pointStrategyDO.setDowngradeRate(100);
        pointStrategyDO.setCreatorName("易振强");
        pointStrategyDO.setCreatorEmail("yizhenqiang@didichuxing.com");
        pointStrategyDO.setOperatorName("易振强");
        pointStrategyDO.setOperatorEmail("yizhenqiang@didichuxing.com");

        System.out.println(pointStrategyDao.updatePointStrategy(pointStrategyDO));
    }

    @Test
    public void queryPointStrategyTest() {
        System.out.println(pointStrategyDao.queryPointStrategyBatch("黑马", "mzz-study", Lists.newArrayList("testPoint"),
                "FIRST_GROUP"));
        System.out.println(
                pointStrategyDao.queryPointStrategyBatch("黑马", "mzz-study", Lists.newArrayList("abcdefPoint"),
                        "FIRST_GROUP"));
    }

    @Test
    public void queryPointStrategyByPageTest() {
        System.out.println(pointStrategyDao.queryPointStrategyByPage("黑马", "mzz-study", "testPoint", null, 0, 10));
    }
}
