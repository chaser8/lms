/**   
 * @Title: CoordinatorServiceTest.java
 * @Package com.tydic.lms.service
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月30日 上午11:18:20
 * @version 
 */
package com.tydic.lms.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tydic.lms.config.Config;
import com.tydic.lms.coordinator.CoordinatorService;
import com.tydic.lms.main.StartUp;
import com.tydic.lms.utils.ZooKeeperUtil;

/**
 * @ClassName: CoordinatorServiceTest
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月30日 上午11:18:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CoordinatorServiceTest {
	private static final Logger log = Logger.getLogger(CoordinatorServiceTest.class);
	@Autowired
    private CoordinatorService coordinatorService;
	@Before
    public void setUp() throws Exception {
		StartUp.class.newInstance();
    }

	

	/**
	 * Test method for {@link com.tydic.lms.coordinator.CoordinatorService#getTasks()}.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test
	public void testGetTasks() throws InstantiationException, IllegalAccessException {
		coordinatorService.initJobs();
		List<String> dirs = ZooKeeperUtil.listChildren(Config.ZK_JOBS_PATH);
		log.info(dirs);
		for (String string : dirs) {
			log.info(ZooKeeperUtil.listChildrenDetail(Config.ZK_JOBS_PATH+"/"+string));
		}
		
	}

}
