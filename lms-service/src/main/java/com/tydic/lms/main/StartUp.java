/**   
 * @Title: StartUp.java
 * @Package com.tydic.lms.main
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月8日 下午5:51:37
 * @version 
 */
package com.tydic.lms.main;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tydic.base.util.Maps;
import com.tydic.base.util.ObjectUtil;
import com.tydic.base.util.SpringBeanUtil;
import com.tydic.lms.config.Config;
import com.tydic.lms.coordinator.CoordinatorService;
import com.tydic.lms.exception.ApplicationException;
import com.tydic.lms.utils.ZooKeeperUtil;

/**
 * @ClassName: StartUp
 * @Description:
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月8日 下午5:51:37
 */
@SuppressWarnings("all")
public class StartUp {
	private static final Logger log = Logger.getLogger(StartUp.class);
	private static SpringBeanUtil springInstance = SpringBeanUtil.getInstance();
	static {
		if (ObjectUtil.isEmpty(springInstance.getApplicationContext())) {
			springInstance.setApplicationContext(new ClassPathXmlApplicationContext("applicationContext.xml"));
		}
		initializeConfig();
		//初始化zk连接
		ZooKeeperUtil.initZkClient(Config.getConfig(Config.ZK_CONNECTSTRING));
	}
	/**
	 * 
	 * @Title: initializeConfig
	 * @Description:  初始化系统配置参数
	 * <br>return type: void 
	 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
	 * @date 2017年3月24日 上午9:31:04
	 */
	private static void initializeConfig(){
		Map<String,String> config = Maps.newHashMap();
		log.info("load appconfig.xml...");
		for (String configKey : Config.configKeys) {
			String propertyValue = springInstance.getLocalProperty(configKey);
			if(ObjectUtil.isEmpty(propertyValue)){
				throw new ApplicationException(String.format("\"%s\" can not be empty in appconfig.xml.", configKey));
			}
			config.put(configKey, propertyValue);
			log.info(String.format("%s : %s",configKey,propertyValue));
		}
		Config.setAppConfig(config);
	}

	/**
	 * @Title: main
	 * @Description:
	 * @param args
	 *            <br>
	 * 			return type: void
	 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
	 * @date 2017年3月8日 下午5:51:37
	 */
	public static void main(String[] args) {
		//主节点
		if("true".equalsIgnoreCase(Config.getConfig(Config.APP_COORDINATOR))){
			CoordinatorService coordinatorService = new CoordinatorService();
			//初始化ZK目录
			coordinatorService.init();
			//加载执行任务的jars
			coordinatorService.initJobs();
		}
	}
}