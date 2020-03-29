package com.didiglobal.sds.admin.test.controller;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.admin.controller.SdsSchemeController;
import com.didiglobal.sds.admin.controller.request.SdsSchemeRequest;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class SdsSchemeControllerTest {

    @Autowired
    private SdsSchemeController sdsSchemeController;

    @Test
    public void querySdsSchemeByPageTest() {

        SdsSchemeRequest request = new SdsSchemeRequest();
        request.setAppGroupName("BikeBusinessDepartment");
        request.setAppName("order");
        request.setSdsSchemeName("默认降级策略");
        request.setOperatorName("路飞");
        request.setOperatorEmail("manzhizhen@didiglobal.com");
        request.setCreatorName("路飞");
        request.setCreatorEmail("manzhizhen@didiglobal.com");
        request.setPage(1);
        request.setPageSize(10);


        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(sdsSchemeController).build();
        try {
            mockMvc.perform(post("/sds/sdsscheme/listpage").characterEncoding("UTF-8").contentType(
                    MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(request))
            ).andDo(print());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
