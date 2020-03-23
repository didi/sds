package com.didiglobal.sds.admin.test.dao;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.admin.dao.SdsSchemeDao;
import com.didiglobal.sds.admin.dao.bean.SdsSchemeDO;
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
public class SdsSchemeDaoTest {

    @Autowired
    private SdsSchemeDao sdsSchemeDao;

    @Test
    public void addSdsSchemeTest() {
        SdsSchemeDO sdsSchemeDO = new SdsSchemeDO();
        sdsSchemeDO.setAppGroupName("BikeBusinessDepartment");
        sdsSchemeDO.setAppName("bh-order");

        sdsSchemeDO.setSdsSchemeName("FIRST_GROUP");
        sdsSchemeDO.setCreatorName("路飞");
        sdsSchemeDO.setCreatorEmail("manzhizhen@didichuxing.com");
        sdsSchemeDO.setOperatorName("路飞");
        sdsSchemeDO.setOperatorEmail("manzhizhen@didichuxing.com");
        Assert.assertEquals(1, sdsSchemeDao.addSdsScheme(sdsSchemeDO));

        sdsSchemeDO.setSdsSchemeName("SECOND_GROUP");
        Assert.assertEquals(1, sdsSchemeDao.addSdsScheme(sdsSchemeDO));
    }

    @Test
    public void queryAllSdsSchemeTest() {
        System.out.println(JSON.toJSONString(sdsSchemeDao.queryAllSdsScheme("BikeBusinessDepartment", "order")));
    }

    @Test
    public void queryByGroupNameTest() {
        System.out.println(sdsSchemeDao.queryByGroupName("BikeBusinessDepartment", "bh-order", "FIRST_GROUP"));
    }

    @Test
    public void updateSdsSchemeTest() {
        System.out.println(sdsSchemeDao.updateSdsScheme("BikeBusinessDepartment", "bh-order", "FIRST_GROUP", "FIRST_GROUP1", "路飞",
                "manzhizhen@didichuxing.com"));
    }

    @Test
    public void deleteSdsSchemeTest() {
        System.out.println(sdsSchemeDao.deleteSdsScheme("BikeBusinessDepartment", "bh-order", "FIRST_GROUP"));
    }

}
