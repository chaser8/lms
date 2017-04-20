package com.tydic.distributed.schedule;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

/**
 * Created by wangcs on 2016/12/16.
 */
public class ScheduleJobUtils {
	private static final Logger log = Logger.getLogger(ScheduleJobUtils.class);

    /**
     * 通过反射调用scheduleJob中定义的方法
     * @param job
     */
    public static Object invokMethod(Schedule job) {
        Class clazz = null;//调用类
        try {
        	clazz = job.getClassLoader().loadClass(job.getBeanClass());
//            clazz = Class.forName(job.getBeanClass());
        } catch (Exception e) {
        	log.error("任务 = [" + job.getScheduleJobKey() + "]---------------未启动成功,调用Class加载异常!!!",e);
            return null;
        }
        Method method = null;//调用方法
        try {
            if(job.getArg()!=null){
                method = clazz.getDeclaredMethod(job.getMethodName(),job.getArg().getClass());
            }else{
                method = clazz.getDeclaredMethod(job.getMethodName());
            }
        } catch (Exception e) {
        	log.error("任务 = [" + job.getScheduleJobKey() + "]---------------未启动成功，方法名设置错误!!!",e);
            return null;
        }

        Object inst = null;//调用对象
        if(!Modifier.isStatic(method.getModifiers())){
            //非静态方法，需要对象
            try {
                inst = clazz.newInstance();
            } catch (Exception e) {
            	log.error("任务 = [" + job.getScheduleJobKey() + "]---------------未启动成功,无法实例化调用对象!!!",e);
                return null;
            }
        }
        try {
            if(job.getArg()!=null) {
            	return method.invoke(inst, job.getArg());
            }else{
            	return method.invoke(inst);
            }
        } catch (Exception e) {
        	log.error("调度["+job.getScheduleJobKey()+"]出错!",e);
        }
        return null;
    }
}

