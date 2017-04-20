package com.tydic.distributed.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import com.tydic.base.util.SpringBeanUtil;
import com.tydic.distributed.coordinator.Job;


public class ScheduleService {
	private static final Logger log = Logger.getLogger(Job.class);
	private org.quartz.Scheduler scheduler;
	/**
	 * @Title: ScheduleService
	 * @Description:  
	 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
	 * @date 2017年4月19日 下午3:37:35
	 */
	public ScheduleService() {
		 scheduler = (org.quartz.Scheduler)SpringBeanUtil.getInstance().getBean("scheduler", org.quartz.Scheduler.class);
	}

    public void addJob(Schedule job) throws Exception{
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getScheduleJobName(), job.getScheduleJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger==null){
            //先构造表达式（避免出错）
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpr())
                    .withMisfireHandlingInstructionDoNothing();
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getScheduleJobName(), job.getScheduleJobGroup()).withSchedule(scheduleBuilder).build();

            //将job对象缓存
            Class clazz = job.hasState() ? QuartzJobFactoryDisallowConcurrentExecution.class : QuartzJobFactory.class;
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getScheduleJobName(), job.getScheduleJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("添加Job==>"+job.toString());
        }else{
            //已存在，更新相应时间
            updateJobCron(job);
        }
    }

    /**
     * 获取所有计划中的任务列表
     * @return Map
     *      key:jobGroup
     *      value.k:jobName
     *      value.v: job状态
     * @throws SchedulerException
    */
    public Map<String,Map<String,Schedule>> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        Map<String,Map<String,Schedule>> jobMap = new HashMap<>();
        for (JobKey jobKey : jobKeys) {
            String group = jobKey.getGroup();
            Map<String,Schedule> map = jobMap.get(group);
            if(map==null){
                map = new HashMap<>();
                jobMap.put(group,map);
            }
            Schedule job = (Schedule)scheduler.getJobDetail(jobKey).getJobDataMap().get("scheduleJob");
            TriggerKey tk = scheduler.getTriggersOfJob(jobKey).get(0).getKey();//一个job只有一种触发规则
            job.setScheduleJobState(scheduler.getTriggerState(tk).name());
            map.put(jobKey.getName(),job);
        }
        return jobMap;
    }

    /**
     * 所有正在运行的job
     * @return
     * @throws SchedulerException
     */
    public List<Schedule> getRunningJob() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<Schedule> jobList = new ArrayList<Schedule>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            JobDetail jobDetail = executingJob.getJobDetail();
            Trigger trigger = executingJob.getTrigger();
            Schedule job = (Schedule)jobDetail.getJobDataMap().get("scheduleJob");
            job.setScheduleJobState(scheduler.getTriggerState(trigger.getKey()).name());
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     * @param job
     * @throws SchedulerException
     */
    public void pauseJob(Schedule job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.getScheduleJobName(), job.getScheduleJobGroup());
        scheduler.pauseJob(jobKey);
        log.info("暂停Job==>"+job.toString());
    }

    /**
     * 恢复一个job
     * @param job
     * @throws SchedulerException
     */
    public void resumeJob(Schedule job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.getScheduleJobName(), job.getScheduleJobGroup());
        scheduler.resumeJob(jobKey);
        log.info("恢复Job==>"+job.toString());
    }

    /**
     * 删除一个job
     * @param job
     * @throws SchedulerException
     */
    public void deleteJob(Schedule job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.getScheduleJobName(), job.getScheduleJobGroup());
        scheduler.deleteJob(jobKey);
        log.info("删除Job==>"+job.toString());
    }

    /**
     * 立即执行job
     * @param job
     * @throws SchedulerException
     */
    public void runAJobNow(Schedule job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.getScheduleJobName(), job.getScheduleJobGroup());
        scheduler.triggerJob(jobKey);
        log.info("立即执行Job==>"+job.toString());
    }

    /**
     * 获取一个job
     * @param jobName
     * @param jobGroup
     */
    public Schedule getJob(String jobName,String jobGroup) throws SchedulerException{
        Schedule job = null;
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger!=null){
            JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
            job = (Schedule) jobDetail.getJobDataMap().get("scheduleJob");
        }
        return job;
    }

    /**
     * 更新job时间表达式
     *
     * @param job
     * @throws SchedulerException
     */
    public void updateJobCron(Schedule job) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getScheduleJobName(), job.getScheduleJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
        Schedule beforeJob = (Schedule) jobDetail.getJobDataMap().get("scheduleJob");
        if(!beforeJob.equals(job)){
            //触发表达式，beanClass 方法，等任意一个不一样，则不一样
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpr())
                    .withMisfireHandlingInstructionDoNothing();//不触发立即执行
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            scheduler.deleteJob(jobDetail.getKey());//先删除。再添加
            jobDetail.getJobDataMap().clear();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            scheduler.scheduleJob(jobDetail,trigger);//先删除再调度

//            scheduler.rescheduleJob(triggerKey, trigger);//此方法不能更新job元数据
            log.info("更新Job:" +
                    "\n beforeJob==>"+beforeJob.toString() +
                    "\n nowJob==>"+job.toString());
        }
    }
}
