package com.didiglobal.sds.admin.test.controller;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.admin.controller.AppInfoController;
import com.didiglobal.sds.admin.controller.request.AppInfoRequest;
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
public class AppInfoControllerTest {

    @Autowired
    private AppInfoController appInfoController;

    @Test
    public void queryAppInfoByPageTest() {

        AppInfoRequest request = new AppInfoRequest();
        request.setAppGroupName("BikeBusinessDepartment");
        request.setAppName("order");
        request.setSdsSchemeName("默认降级预案");
        request.setOperatorName("路飞");
        request.setOperatorEmail("manzhizhen@didiglobal.com");
        request.setCreatorName("路飞");
        request.setCreatorEmail("manzhizhen@didiglobal.com");
        request.setPage(1);
        request.setPageSize(10);


        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(appInfoController).build();
        try {
            mockMvc.perform(
                    post("/sds/appinfo/listpage").contentType("application/json").content(JSON.toJSONString(request))
            ).andDo(print());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
