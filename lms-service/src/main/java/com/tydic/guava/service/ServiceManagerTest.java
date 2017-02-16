/**   
 * @Title: ConcurrentTest.java
 * @Package com.tydic.guava.service
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年2月7日 下午2:10:09
 * @version 
 */
package com.tydic.guava.service;

import java.util.ArrayList;
import java.util.List;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;

/**
 * @ClassName: ConcurrentTest
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年2月7日 下午2:10:09
 */
public class ServiceManagerTest {
	private ServiceManager sm ;
	private  List<Service> serviceList = new ArrayList<Service>();
	public ServiceManagerTest() {
		serviceList.add(new ServiceTest());
		sm= new ServiceManager(serviceList);
		sm.addListener(new ServiceManager.Listener() {
            @Override
            public void healthy() {
            	System.out.println("----------------healthy-------------");
                super.healthy();
            }

            @Override
            public void stopped() {
            	System.out.println("----------------stopped-------------");
                super.stopped();
            }

            @Override
            public void failure(Service service) {
                System.out.println("服务[" + service.getClass().getSimpleName() + "]出现异常!" + service.state());
            }
        });
		for (Service service : serviceList) {
			service.startAsync();
		}
	}
}
