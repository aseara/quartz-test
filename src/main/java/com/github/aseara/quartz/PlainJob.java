package com.github.aseara.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by qiujingde on 2017/2/21.
 * 普通任务类，不需要实现quartz接口。
 */
@Component
public class PlainJob {

    private static final Logger LOG = LoggerFactory.getLogger(PlainJob.class);

    /**
     * 需要执行的任务。
     */
    public void run() {
        LOG.info("Hello World! - " + new Date());
    }

}
