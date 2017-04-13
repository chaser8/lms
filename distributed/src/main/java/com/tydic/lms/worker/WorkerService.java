/**   
 * @Title: WorkerService.java
 * @Package com.tydic.lms.service
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午10:03:38
 * @version 
 */
package com.tydic.lms.worker;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import com.tydic.lms.config.Config;
import com.tydic.lms.utils.ZooKeeperUtil;

/**
 * @ClassName: WorkerService
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午10:03:38
 */
public class WorkerService {
	private static final Logger log = Logger.getLogger(WorkerService.class);
	public void init(){
		/** 工作节点 */
		ZooKeeperUtil.createNode(Config.ZK_WORKER_PATH, "",CreateMode.PERSISTENT_SEQUENTIAL);
		log.info(String.format("%s created.", Config.ZK_WORKER_PATH));
	}
}