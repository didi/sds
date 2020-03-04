package com.didiglobal.sds.admin.test.dao;

import com.didiglobal.sds.admin.dao.PointDictDao;
import com.didiglobal.sds.admin.dao.bean.PointDictDO;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by tianyulei on 2019/7/10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PointDictDaoTest {

    @Autowired
    private PointDictDao pointDictDao;

    @Test
    public void testInsert(){
        PointDictDO pointDictDO = new PointDictDO();

        pointDictDO.setAppName("t1");
        pointDictDO.setAppGroupName("tg1");
        pointDictDO.setPoint("testPointDict2");
        pointDictDao.addPointDict(pointDictDO);
    }

    @Test
    public void testdelete(){
        pointDictDao.deletePointDictList(Lists.newArrayList(442L));
    }
}
