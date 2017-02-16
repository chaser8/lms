/**   
 * @Title: ServiceTest.java
 * @Package com.tydic.guava.service
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年2月7日 下午2:16:42
 * @version 
 */
package com.tydic.guava.service;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * @ClassName: ServiceTest
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年2月7日 下午2:16:42
 */
public class ServiceTest extends AbstractExecutionThreadService {
	 private Runnable fetch;
	 private final Object lock = new Object();
	 
	@Override
	protected void run() throws Exception {
		System.out.println("-------------run--------------");
		
		 //每次执行完一个任务，启动执行一个新任务
        fetch = new Runnable(){
            @Override
            public void run(){
            	System.out.println("---------------Runnable run---------");
                try {
                	System.out.println("---------------Runnable sleep---------");
					Thread.sleep(5000);
					prepareStop();
				} catch (Exception e) {
					e.printStackTrace();
				}
                startThreadSubmitTask();
            }
        };
        for (int i = 0; i < 1; i++) {
            startThreadSubmitTask();
        }

        synchronized (lock) {
            lock.wait();//服务挂起到后台
        }
        System.out.println("111");
	}
	
    /**
     * 唤醒，准备停止
     */
    public final void prepareStop() {
        synchronized (lock) {
            lock.notify();
        }
    }
	
	/**
     * 异步提交一个任务
     */
    private void startThreadSubmitTask(){
        if(isRunning()){
            //此处不用递归，是防止堆栈溢出
            Thread th = new Thread(fetch);
            System.out.println("---------------Runnable start new---------");
            th.start();
            th = null;
        }
    }

}
