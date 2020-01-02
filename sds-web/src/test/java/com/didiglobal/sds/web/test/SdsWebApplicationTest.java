package com.didiglobal.sds.web.test;

import com.didiglobal.sds.web.SdsWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:config/application.properties")
public class SdsWebApplicationTest {

    @Test
    public void mainTest () {
        SdsWebApplication.main(new String[]{});
    }
}
