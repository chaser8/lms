/**   
 * @Title: Config.java
 * @Package com.tydic.lms.config
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午9:57:24
 * @version 
 */
package com.tydic.lms.config;

import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.lms.exception.ApplicationException;

/**
 * @ClassName: Config
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午9:57:24
 */
public class Config {
	
	private static final Logger log = Logger.getLogger(Config.class);
	public static final String APP_COORDINATOR = "coordinator";
	public static final String APP_JARS = "jars";
	
	public static final String ZK_CONNECTSTRING = "zk.connectString";
	//主节点
	public static final String ZK_ROOT_PATH = "/lms-service";
	/** 主节点管理目录 */
	public static final String ZK_COORDINATION_PATH = ZK_ROOT_PATH+"/coordination";
	public static final String ZK_COORDINATOR_PATH = ZK_COORDINATION_PATH+"/coordinator";
	
	/** 执行任务的节点管理目录 */
	public static final String ZK_WORKERS_PATH = ZK_ROOT_PATH+"/workers";
	public static final String ZK_WORKER_PATH = ZK_COORDINATION_PATH+"/worker";
	
	/** 实际执行的任务管理目录 */
	public static final String ZK_TASKS_PATH = ZK_ROOT_PATH+"/tasks";
	public static final String ZK_TASK_PATH = ZK_TASKS_PATH+"/task";
	
	/**
	 * 用户上传的任务目录
	 */
	public static final String ZK_JOBS_PATH = ZK_ROOT_PATH+"/jbos";
	public static final String ZK_JOB_PATH = ZK_JOBS_PATH+"/job";
	
	private static Map<String,String> config;
	
	public static void setAppConfig(Map<String,String> config){
		Config.config = config;
	}
	public static String getConfig(String key){
		if(config==null){
			throw new ApplicationException("config is null.");
		}
		return Config.config.get(key);
	}
	public static final String [] configKeys=new String[]{
			APP_COORDINATOR,ZK_CONNECTSTRING,APP_JARS
	};
	
}
