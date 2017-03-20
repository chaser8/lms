/**   
 * @Title: CoordinatorService.java
 * @Package com.tydic.lms.service
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午10:03:14
 * @version 
 */
package com.tydic.lms.service;

import java.util.Map;

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
		ZooKeeperUtil.initZkClient(Config.ZK_ADDRESS);
		ZooKeeperUtil.deleteNode(Config.ZK_WORKER_PATH);
		ZooKeeperUtil.createNode(Config.ZK_ROOT_PATH, "1111",CreateMode.PERSISTENT);
		log.info(String.format("%s created.", Config.ZK_ROOT_PATH));
		ZooKeeperUtil.createNode(Config.ZK_COORDINATION_PATH, "aaaa",CreateMode.PERSISTENT);
		log.info(String.format("%s created.", Config.ZK_COORDINATION_PATH));
		ZooKeeperUtil.createNode(Config.ZK_COORDINATOR_PATH, "",CreateMode.PERSISTENT_SEQUENTIAL);
		log.info(String.format("%s created.", Config.ZK_COORDINATOR_PATH));
		Map m = ZooKeeperUtil.listChildrenDetail(Config.ZK_ROOT_PATH);
		
		log.debug(m);
	}
}