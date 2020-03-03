package com.didiglobal.sds.web.test.controller;

import com.didiglobal.sds.web.controller.SdsSchemeController;
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
    public void queryAppInfoByPageTest() {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(sdsSchemeController).build();
        try {
            mockMvc.perform(post("/sds/strategygroup/listpage").characterEncoding("UTF-8").contentType(
                    MediaType.APPLICATION_JSON_UTF8)
                    .param("appGroupName", "两轮车")
                    .param("appName", "bh-order")
                    .param("page", "1")
                    .param("pageSize", "10")
            ).andDo(print());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
