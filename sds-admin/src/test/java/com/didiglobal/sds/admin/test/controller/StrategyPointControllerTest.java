package com.didiglobal.sds.admin.test.controller;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.admin.controller.PointStrategyController;
import com.didiglobal.sds.admin.controller.request.PointStrategyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by tianyulei on 2019/6/16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyPointControllerTest {

    @Autowired
    private PointStrategyController pointStrategyController;

    @Test
    public void queryAppCurSdsSchemeTipsTest() throws Exception {

        PointStrategyRequest request = new PointStrategyRequest();
        request.setAppGroupName("应用组A");
        request.setAppName("应用a");
        request.setPage(1);
        request.setPageSize(10);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(pointStrategyController).build();
        mockMvc.perform(
                post("/sds/pointstrategy/add").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(request))
        ).andDo(print());
    }
}
