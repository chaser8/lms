package com.tydic.distributed.schedule;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 有状态任务（若一个任务执行不完，下次轮到时则等待方法执行完后再执行）
 * 注解实现
 * Created by wangcs on 2016/12/16.
 */
@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job {
	 private static final Logger log = Logger.getLogger(QuartzJobFactoryDisallowConcurrentExecution.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Schedule scheduleJob = (Schedule) context.getMergedJobDataMap().get("scheduleJob");
        scheduleJob.callBack(ScheduleJobUtils.invokMethod(scheduleJob));
    }
}