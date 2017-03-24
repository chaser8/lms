/**   
 * @Title: CoordinatorService.java
 * @Package com.tydic.lms.service
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午10:03:14
 * @version 
 */
package com.tydic.lms.service;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import com.tydic.lms.config.Config;
import com.tydic.lms.utils.ZooKeeperUtil;

/**
 * @ClassName: CoordinatorService
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午10:03:14
 */
public class CoordinatorService {
	private static final Logger log = Logger.getLogger(CoordinatorService.class);
	public void init(){
		/** 节点管理目录 */
		ZooKeeperUtil.createNode(Config.ZK_ROOT_PATH, "",CreateMode.PERSISTENT);
		log.info(String.format("%s created.", Config.ZK_ROOT_PATH));
		ZooKeeperUtil.createNode(Config.ZK_COORDINATION_PATH, "",CreateMode.PERSISTENT);
		log.info(String.format("%s created.", Config.ZK_COORDINATION_PATH));
		ZooKeeperUtil.createNode(Config.ZK_COORDINATOR_PATH, "",CreateMode.PERSISTENT_SEQUENTIAL);
		log.info(String.format("%s created.", Config.ZK_COORDINATOR_PATH));
	}
}