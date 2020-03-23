package com.didiglobal.sds.admin.test.dao;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.admin.dao.AppGroupDao;
import com.didiglobal.sds.admin.dao.bean.AppGroupDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by manzhizhen on 18/2/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppGroupDaoTest {

    @Autowired
    private AppGroupDao appGroupDao;

    @Test
    public void addAppGroupTest() {
        AppGroupDO appGroupDO = new AppGroupDO();
        appGroupDO.setCreatorName("路飞");
        appGroupDO.setOperatorName("路飞");
        appGroupDO.setCreatorEmail("manzhizhen@didichuxing.com");
        appGroupDO.setOperatorEmail("manzhizhen@didichuxing.com");
        appGroupDO.setAppGroupName("黑马4");
        Assert.assertEquals(1, appGroupDao.addAppGroup(appGroupDO));

        appGroupDO.setAppGroupName("黑马5");
        Assert.assertEquals(1, appGroupDao.addAppGroup(appGroupDO));

        appGroupDO.setAppGroupName("黑马6");
        Assert.assertEquals(1, appGroupDao.addAppGroup(appGroupDO));
    }

    @Test
    public void queryAllAppGroupTest() {
        System.out.println(JSON.toJSONString(appGroupDao.queryAllAppGroup()));
    }

    @Test
    public void queryByGroupNameTest() {
        System.out.println(appGroupDao.queryByGroupName("黑马1"));
    }

    @Test
    public void queryAppGroupByPageTest() {
        System.out.println(appGroupDao.queryAppGroupByPage("    ", 1, 10));
    }


}
