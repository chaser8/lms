/**   
 * @Title: CoordinatorService.java
 * @Package com.tydic.lms.service
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午10:03:14
 * @version 
 */
package com.tydic.lms.coordinator;

import java.io.File;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.tydic.base.util.FileUtil;
import com.tydic.lms.config.Config;
import com.tydic.lms.utils.JarUtil;
import com.tydic.lms.utils.ZooKeeperUtil;

/**
 * @ClassName: CoordinatorService
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月16日 上午10:03:14
 */
@Service
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
		/**工作节点根目录*/
		ZooKeeperUtil.createNode(Config.ZK_WORKERS_PATH, "",CreateMode.PERSISTENT);
		log.info(String.format("%s created.", Config.ZK_WORKERS_PATH));
		/**任务节点根目录*/
		ZooKeeperUtil.createNode(Config.ZK_TASKS_PATH, "",CreateMode.PERSISTENT);
		log.info(String.format("%s created.", Config.ZK_TASKS_PATH));
	}
	public void initJobs(){
		ZooKeeperUtil.createNode(Config.ZK_JOBS_PATH, "",CreateMode.PERSISTENT);
		log.info(String.format("%s created.", Config.ZK_TASKS_PATH));
		
		File[] files = FileUtil.getFiles(Config.getConfig(Config.APP_JARS));
		for (File file : files) {
			if(!FileUtil.getExtension(file.getName().toLowerCase()).endsWith("jar")){
				continue;
			}
			try {
				URLClassLoader classLoader = JarUtil.loadJar(Thread.currentThread(), "file:"+file.getPath());
				InputStream configIs = JarUtil.getResourceAsStream(new JarFile(file.getPath()), "config.properties");
				log.info(new String(StreamUtils.copyToByteArray(configIs)));
				ZooKeeperUtil.createNode(Config.ZK_JOB_PATH, new String(StreamUtils.copyToByteArray(configIs)),CreateMode.EPHEMERAL_SEQUENTIAL);
				log.info(String.format("load task jar %s",file.getPath()));
			} catch (Exception e) {
				log.error(String.format("load %s error!",file.getPath()),e);
			}
		}
	}
	
	public void geTasks(){
		File[] files = FileUtil.getFiles(Config.getConfig(Config.APP_JARS));
		for (File file : files) {
			try {
				JarUtil.loadJar(Thread.currentThread(), "file:"+file.getPath());
				log.info(String.format("load task jar %s",file.getPath()));
			} catch (Exception e) {
				log.error(String.format("load %s error!",file.getPath()),e);
			}
		}
	}
}