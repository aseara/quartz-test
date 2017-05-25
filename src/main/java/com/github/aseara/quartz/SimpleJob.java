package com.github.aseara.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by qiujingde on 2017/2/21.
 * a simple job
 */
public class SimpleJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        LOG.info("SimpleJob says: " + jobKey + " executing at " + new Date());
    }
}
