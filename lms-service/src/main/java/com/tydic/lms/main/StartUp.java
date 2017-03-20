/**   
 * @Title: StartUp.java
 * @Package com.tydic.lms.main
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月8日 下午5:51:37
 * @version 
 */
package com.tydic.lms.main;

import com.tydic.lms.config.Config;
import com.tydic.lms.service.CoordinatorService;
import com.tydic.lms.utils.ZooKeeperUtil;

/**
 * @ClassName: StartUp
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月8日 下午5:51:37
 */
public class StartUp {

	/**
	 * @Title: main
	 * @Description: 
	 * @param args 
	 * <br>return type: void 
	 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
	 * @date 2017年3月8日 下午5:51:37
	 */
	public static void main(String[] args) {
		ZooKeeperUtil.initZkClient(Config.ZK_ADDRESS);
		CoordinatorService coordinatorService = new CoordinatorService();
		coordinatorService.init();
	}
}