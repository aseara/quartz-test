package com.github.aseara.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by qiujingde on 2017/2/21.
 * a simple job
 */
public class HelloJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(HelloJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Hello World! - " + new Date());
    }
}
