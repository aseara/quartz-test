package com.github.aseara.quartz;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.evenSecondDateAfterNow;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by qiujingde on 2017/2/21.
 * Quartz测试。
 */
public class QuartzTest2 {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzTest2.class);

    private static Scheduler sched;

    @BeforeClass
    public static void start() throws SchedulerException {
        sched = StdSchedulerFactory.getDefaultScheduler();
    }

    /**
     * hello world
     * @throws SchedulerException     scheduler exception
     * @throws InterruptedException   thread sleep exception
     */
    @Test
    public void helloJob() throws SchedulerException, InterruptedException {

        Date runTime = evenSecondDateAfterNow();

        JobDetail job = newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();

        sched.scheduleJob(job, trigger);

        sched.start();
        Thread.sleep(30 * 1000);
    }

    /**
     * demonstrate all of the basics of scheduling capabilities of Quartz using Simple Triggers
     * @throws SchedulerException     scheduler exception
     * @throws InterruptedException   thread sleep exception
     */
    @Test
    public void simpleTriggerJob() throws SchedulerException, InterruptedException {

        Date startTime = nextGivenSecondDate(null, 15);

        // jobs can be scheduled before sched.start() has been called
        // job1 will only fire once at date/time "ts"
        JobDetail job = newJob(SimpleJob.class)
                .withIdentity("job1", "group1")
                .build();
        SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(startTime)
                .build();

        Date ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // job2 will only fire once at date/time "ts"
        job = newJob(SimpleJob.class)
                .withIdentity("job2", "group1")
                .build();
        trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger2", "group1")
                .startAt(startTime)
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // job3 will run 11 times (run once and repeat 10 more times)
        // job3 will repeat every 10 seconds
        job = newJob(SimpleJob.class)
                .withIdentity("job3", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger3", "group1")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(10))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");


        // the same job (job3) will be scheduled by a another trigger
        // this time will only repeat twice at a 10 second interval
        trigger = newTrigger()
                .withIdentity("trigger3", "group2")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(2))
                .forJob(job)
                .build();

        ft = sched.scheduleJob(trigger);
        LOG.info(job.getKey() + " will [also] run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // job4 will run 6 times (run once and repeat 5 more times)
        // job4 will repeat every 10 seconds
        job = newJob(SimpleJob.class)
                .withIdentity("job4", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger4", "group1")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(5))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // job5 will run once, five SECOND in the future
        job = newJob(SimpleJob.class)
                .withIdentity("job5", "group1")
                .build();
        trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger5", "group1")
                .startAt(futureDate(5, DateBuilder.IntervalUnit.SECOND))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // job6 will run indefinitely, every 40 seconds
        job = newJob(SimpleJob.class)
                .withIdentity("job6", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger6", "group1")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(40)
                        .repeatForever())
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // All of the jobs have been added to the scheduler, but none of the jobs
        // will run until the scheduler has been started
        sched.start();

        // jobs can also be scheduled after start() has been called...
        // job7 will repeat 20 times, repeat every five minutes
        job = newJob(SimpleJob.class)
                .withIdentity("job7", "group1")
                .build();

        trigger = newTrigger()
                .withIdentity("trigger7", "group1")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(5)
                        .withRepeatCount(20))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // jobs can be fired directly... (rather than waiting for a trigger)
        job = newJob(SimpleJob.class).withIdentity("job8", "group1").storeDurably().build();

        sched.addJob(job, true);
        sched.triggerJob(jobKey("job8", "group1"));

        Thread.sleep(30L * 1000L);
    }

    /**
     * demonstrate all of the basics of scheduling capabilities of Quartz using Cron Triggers
     * @throws SchedulerException     scheduler exception
     * @throws InterruptedException   thread sleep exception
     */
    @Test
    public void simpleCronTriggerJob() throws SchedulerException, InterruptedException {

        // job 1 will run every 20 seconds
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

        // job 2 will run every other minute (at 15 seconds past the minute)
        job = newJob(SimpleJob.class)
                .withIdentity("job2", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger2", "group1")
                .withSchedule(cronSchedule("15 0/2 * * * ?"))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        // job 3 will run every even minute but only between 8am and 5pm
        job = newJob(SimpleJob.class)
                .withIdentity("job3", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(cronSchedule("0 0/2 8-17 * * ?"))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        // job 4 will run every three minutes but only between 5pm and 11pm
        job = newJob(SimpleJob.class)
                .withIdentity("job4", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger4", "group1")
                .withSchedule(cronSchedule("0 0/3 17-23 * * ?"))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        // job 5 will run at 10am on the 1st and 15th days of the month
        job = newJob(SimpleJob.class)
                .withIdentity("job5", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger5", "group1")
                .withSchedule(cronSchedule("0 0 10am 1,15 * ?"))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        // job 6 will run every 30 seconds but only on Weekdays (Monday through Friday)
        job = newJob(SimpleJob.class)
                .withIdentity("job6", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger6", "group1")
                .withSchedule(cronSchedule("0,30 * * ? * MON-FRI"))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        // job 7 will run every 30 seconds but only on Weekends (Saturday and Sunday)
        job = newJob(SimpleJob.class)
                .withIdentity("job7", "group1")
                .build();
        trigger = newTrigger()
                .withIdentity("trigger7", "group1")
                .withSchedule(cronSchedule("0,30 * * ? * SAT,SUN"))
                .build();

        ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
                + trigger.getCronExpression());

        // All of the jobs have been added to the scheduler, but none of the
        // jobs will run until the scheduler has been started
        sched.start();

        Thread.sleep(30 * 1000);
    }


    @AfterClass
    public static void shutdown() throws SchedulerException {
        sched.shutdown();
        SchedulerMetaData metaData = sched.getMetaData();
        LOG.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

}
