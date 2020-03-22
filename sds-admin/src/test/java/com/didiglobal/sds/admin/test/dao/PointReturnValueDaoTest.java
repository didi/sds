package com.didiglobal.sds.admin.test.dao;

import com.didiglobal.sds.admin.dao.PointReturnValueDao;
import com.didiglobal.sds.admin.dao.bean.PointReturnValueDO;
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
public class PointReturnValueDaoTest {

    @Autowired
    private PointReturnValueDao pointReturnValueDao;


    @Test
    public void addPointReturnValueTest() {

        PointReturnValueDO pointReturnValueDO = new PointReturnValueDO();

        pointReturnValueDO.setAppGroupName("htw");
        pointReturnValueDO.setAppName("测试");
        pointReturnValueDO.setPoint("testPoint");
        pointReturnValueDO.setStatus(1);
        pointReturnValueDO.setReturnValueStr("abc-json");
        pointReturnValueDO.setOperatorName("manzhizhen");
        pointReturnValueDO.setOperatorEmail("manzhizhen@didichuxing.com");
        pointReturnValueDO.setCreatorName("manzhizhen");
        pointReturnValueDO.setCreatorEmail("manzhizhen@didichuxing.com");

        pointReturnValueDao.addPointReturnValue(pointReturnValueDO);
    }


    @Test
    public void updatePointReturnValueTest() {

        pointReturnValueDao.updatePointReturnValue("htw", "测试", "testPoint",
                "new-abc-json", 0, "manzhizhen", "manzhizhen@didichuxing.com");
    }


    @Test
    public void deletePointReturnValueTest() {

        pointReturnValueDao.deletePointReturnValue("htw", "测试", "testPoint");
    }

    @Test
    public void queryPointReturnValueByPageTest() {
        System.out.println(
                pointReturnValueDao.queryPointReturnValueByPage("hm", "yzq-dubbo1", "sdfsdf", "sdfsdf", 0, 10));
    }

}
