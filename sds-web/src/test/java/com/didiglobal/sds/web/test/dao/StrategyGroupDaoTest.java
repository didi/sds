package com.didiglobal.sds.web.test.dao;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.web.dao.StrategyGroupDao;
import com.didiglobal.sds.web.dao.bean.StrategyGroupDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yizhenqiang on 18/2/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyGroupDaoTest {

    @Autowired
    private StrategyGroupDao strategyGroupDao;

    @Test
    public void addStrategyGroupTest() {
        StrategyGroupDO strategyGroupDO = new StrategyGroupDO();
        strategyGroupDO.setAppGroupName("黑马");
        strategyGroupDO.setAppName("bh-order");

        strategyGroupDO.setStrategyGroupName("FIRST_GROUP");
        strategyGroupDO.setCreatorName("易振强");
        strategyGroupDO.setCreatorEmail("yizhenqiang@didichuxing.com");
        Assert.assertEquals(1, strategyGroupDao.addStrategyGroup(strategyGroupDO));

        strategyGroupDO.setStrategyGroupName("SECOND_GROUP");
        Assert.assertEquals(1, strategyGroupDao.addStrategyGroup(strategyGroupDO));
    }

    @Test
    public void queryAllStrategyGroupTest() {
        System.out.println(JSON.toJSONString(strategyGroupDao.queryAllStrategyGroup("黑马", "mzz-study")));
    }

    @Test
    public void queryByGroupNameTest() {
        System.out.println(strategyGroupDao.queryByGroupName("黑马", "bh-order", "FIRST_GROUP"));
    }

    @Test
    public void updateStrategyGroupTest() {
        System.out.println(strategyGroupDao.updateStrategyGroup("黑马", "bh-order", "FIRST_GROUP", "FIRST_GROUP1", "易振强",
                "yizhenqiang@didichuxing.com"));
    }

    @Test
    public void deleteStrategyGroupTest() {
        System.out.println(strategyGroupDao.deleteStrategyGroup("黑马", "bh-order", "FIRST_GROUP"));
    }

}
