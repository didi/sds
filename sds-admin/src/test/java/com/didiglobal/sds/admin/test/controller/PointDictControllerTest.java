package com.didiglobal.sds.admin.test.controller;

import com.didiglobal.sds.admin.controller.PointDictController;
import com.didiglobal.sds.admin.controller.request.PointDictRequest;
import com.didiglobal.sds.admin.controller.response.SdsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by tianyulei on 2019/8/11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PointDictControllerTest {

    @Autowired
    private PointDictController pointDictController;

    @Test
    public void testQueryList() {
        PointDictRequest pointDictRequest = new PointDictRequest();
        pointDictRequest.setAppGroupName("htw");
        pointDictRequest.setAppName("htw-user");
        SdsResponse<List<String>> list = pointDictController.queryPointDictList(pointDictRequest);
        System.out.println(list);
    }
}
