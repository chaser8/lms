/**   
 * @Title: ClassUtil.java
 * @Package com.tydic.lms.utils
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月30日 上午10:07:38
 * @version 
 */
package com.tydic.lms.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

/**
 * @ClassName: ClassUtil
 * @Description:
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月30日 上午10:07:38
 */
public class ClassUtil {
	private static final Logger log = Logger.getLogger(ClassUtil.class);
	public static Class getClass(URLClassLoader classLoader,String name) throws ClassNotFoundException{
		return classLoader.loadClass(name);
	}
}
