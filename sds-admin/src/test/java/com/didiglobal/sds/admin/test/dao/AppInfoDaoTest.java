package com.didiglobal.sds.admin.test.dao;

import com.didiglobal.sds.admin.dao.AppInfoDao;
import com.didiglobal.sds.admin.dao.bean.AppInfoDO;
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
public class AppInfoDaoTest {

    @Autowired
    private AppInfoDao appInfoDao;

    @Test
    public void addAppInfoTest() {
        AppInfoDO appInfoDO = new AppInfoDO();
        appInfoDO.setAppGroupName("黑马1");
        appInfoDO.setAppName("bh-order");
        appInfoDO.setCreatorEmail("manzhizhen@163.com");
        appInfoDO.setCreatorName("manzhizhen");
        appInfoDO.setOperatorEmail("manzhizhen@163.com");
        appInfoDO.setOperatorName("manzhizhen");
        Assert.assertEquals(1, appInfoDao.addAppInfo(appInfoDO));

        appInfoDO.setAppGroupName("黑马1");
        appInfoDO.setAppName("bh-order1");
        Assert.assertEquals(1, appInfoDao.addAppInfo(appInfoDO));

        appInfoDO.setAppGroupName("黑马1");
        appInfoDO.setAppName("bh-order1");
        try {
            appInfoDao.addAppInfo(appInfoDO);
            Assert.fail();
        } catch (Exception e) {
        }

        appInfoDO.setAppGroupName("黑马2");
        appInfoDO.setAppName("bh-order");
        appInfoDO.setSdsSchemeName("FIRST_GREP");
        Assert.assertEquals(1, appInfoDao.addAppInfo(appInfoDO));
    }

    @Test
    public void queryAppInfoTest() {
        System.out.println(appInfoDao.queryAppInfo("BikeBusinessDepartment", "bh-order"));
    }

    @Test
    public void queryAppInfoByPageTest() {
        System.out.println(appInfoDao.queryAppInfoByPage(null, null, 1, 10));
    }
}
