package com.github.aseara.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by qiujingde on 2017/2/21.
 * spring + quartz测试。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:quartz.xml")
public class SpringQuartzTest {

    /**
     * 简单的spring+quartz组合测试。
     */
    @Test
    public void hello() throws InterruptedException {
        Thread.sleep(30 * 1000);
    }

}
