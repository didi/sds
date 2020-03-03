package com.didiglobal.sds.web.test.controller;

import com.didiglobal.sds.web.controller.PointStrategyController;
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
    public void testAdd() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(pointStrategyController).build();
        mockMvc.perform(
                post("/sds/pointstrategy/add").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("appGroupName", "应用组A")
                        .param("appName", "应用a")
                        .param("point", "eeee")
                        .param("sdsSchemeName", "删除降级预案")
                        .param("operatorName", "1")
                        .param("operatorEmail", "1")
                        .param("creatorName", "1")
                        .param("creatorEmail", "1")
        ).andDo(print());
    }
}
