package com.didiglobal.sds.web.test.controller;

import com.didiglobal.sds.web.controller.AppGroupController;
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
    public void queryAppInfoByPageTest() {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(appGroupController).build();
        try {
            mockMvc.perform(post("/sds/appgroup/listpage").param("appGroupName", "黑马")
                    .param("appName", "bh-order")
                    .param("page", "1")
                    .param("pageSize", "10")
            ).andDo(print());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
