package com.tydic.distributed.schedule;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 任务无状态
 * Created by wangcs on 2016/12/16.
 */
public class QuartzJobFactory implements Job {
	 private static final Logger log = Logger.getLogger(QuartzJobFactory.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Schedule scheduleJob = (Schedule) context.getMergedJobDataMap().get("scheduleJob");
        log.error(scheduleJob);
        scheduleJob.callBack(ScheduleJobUtils.invokMethod(scheduleJob));
    }
}
