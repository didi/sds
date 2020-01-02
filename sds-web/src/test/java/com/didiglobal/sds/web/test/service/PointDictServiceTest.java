package com.didiglobal.sds.web.test.service;

import com.didiglobal.sds.client.bean.SdsCycleInfo;
import com.didiglobal.sds.web.dao.bean.PointDictDO;
import com.didiglobal.sds.web.service.PointDictService;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tianyulei on 2019/7/15
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PointDictServiceTest {

    @Autowired
    private PointDictService pointDictService;

    @Test
    public void testInsert() {
        PointDictDO pointDictDO = new PointDictDO();
        pointDictDO.setPoint("1");
        pointDictDO.setAppName("1");
        pointDictDO.setAppGroupName("1");

        PointDictDO pointDictDO1 = new PointDictDO();
        pointDictDO1.setPoint("2");
        pointDictDO1.setAppName("2");
        pointDictDO1.setAppGroupName("2");

        PointDictDO pointDictDO2 = new PointDictDO();
        pointDictDO2.setPoint("3");
        pointDictDO2.setAppName("3");
        pointDictDO2.setAppGroupName("3");

        PointDictDO pointDictDO3 = new PointDictDO();
        pointDictDO3.setPoint("4");
        pointDictDO3.setAppName("4");
        pointDictDO3.setAppGroupName("4");

        PointDictDO pointDictDO4 = new PointDictDO();
        pointDictDO4.setPoint("4");
        pointDictDO4.setAppName("4");
        pointDictDO4.setAppGroupName("4");


        List<PointDictDO> list = new ArrayList<>();
        list.add(pointDictDO);
        list.add(pointDictDO1);
        list.add(pointDictDO2);
        list.add(pointDictDO3);
        list.add(pointDictDO4);

        pointDictService.addPointList(list);
    }

    @Test
    public void testCheckAndDelete(){
        for(int i = 0; i< 11; i++){
            Map<String, SdsCycleInfo> map = Maps.newHashMap("1", new SdsCycleInfo());

            pointDictService.checkAndDeleteDeadPoint(map, "1", "1");
        }
    }

}
