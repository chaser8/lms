/**   
 * @Title: Config.java
 * @Package com.tydic.lms.config
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午9:57:24
 * @version 
 */
package com.tydic.lms.config;

/**
 * @ClassName: Config
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午9:57:24
 */
public class Config {
	public static final String ZK_ADDRESS = "localhost:2181";
	public static final String ZK_ROOT_PATH = "/lms-service";
	public static final String ZK_COORDINATION_PATH = ZK_ROOT_PATH+"/COORDINATION";
	public static final String ZK_COORDINATOR_PATH = ZK_COORDINATION_PATH+"/OORDINATOR";
	public static final String ZK_WORKER_PATH = ZK_COORDINATION_PATH+"/WORKER";
}
