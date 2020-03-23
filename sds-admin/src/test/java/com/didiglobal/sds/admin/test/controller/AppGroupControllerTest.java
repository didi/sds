package com.didiglobal.sds.admin.test.controller;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.admin.controller.AppGroupController;
import com.didiglobal.sds.admin.controller.request.AppGroupRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppGroupControllerTest {

    @Autowired
    private AppGroupController appGroupController;

    @Test
    public void queryAppGroupByPageTest() {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(appGroupController).build();
        try {
            AppGroupRequest request = new AppGroupRequest();
            request.setAppGroupName("BikeBusinessDepartment");
            request.setOperatorName("路飞");
            request.setOperatorEmail("manzhizhen@didiglobal.com");
            request.setCreatorName("路飞");
            request.setCreatorEmail("manzhizhen@didiglobal.com");
            request.setPage(1);
            request.setPageSize(10);

            mockMvc.perform(post("/sds/appgroup/listpage").contentType("application/json").content(JSON.toJSONString(request))
            ).andDo(print());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
