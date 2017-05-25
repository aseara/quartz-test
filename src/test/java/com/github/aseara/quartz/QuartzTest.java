package com.github.aseara.quartz;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by qiujingde on 2017/2/21.
 * Quartz测试。
 */
public class QuartzTest {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzTest2.class);

    private static Scheduler sched;

    @BeforeClass
    public static void start() throws SchedulerException {
        sched = StdSchedulerFactory.getDefaultScheduler();
    }

    @AfterClass
    public static void shutdown() throws SchedulerException {
        sched.shutdown();
        SchedulerMetaData metaData = sched.getMetaData();
        LOG.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

    /**
     *
     * @throws SchedulerException
     * @throws InterruptedException
     */
    @Test
    public void simpleTriggerJob() throws SchedulerException, InterruptedException {
        // 设置开始时间
        Date startTime = nextGivenSecondDate(null, 15);

        JobDetail job = newJob(SimpleJob.class)
                .withIdentity("job1", "group1")
                .build();
        SimpleTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(5)
                        .repeatForever())
                .build();

        Date ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        sched.start();
        Thread.sleep(30L * 1000L);
    }

    @Test
    public void simpleCronTriggerJob() throws SchedulerException, InterruptedException {
        JobDetail job = newJob(SimpleJob.class)
                .withIdentity("job1", "group1")
                .build();
        CronTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule("0/20 * * * * ?"))
                .build();

        Date ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        sched.start();
        Thread.sleep(30L * 1000L);
    }

}
