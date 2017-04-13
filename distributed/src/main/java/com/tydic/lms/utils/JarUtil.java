/**   
 * @Title: JarUtil.java
 * @Package com.tydic.lms.utils
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月31日 下午5:00:00
 * @version 
 */
package com.tydic.lms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * @ClassName: JarUtil
 * @Description: 
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年3月31日 下午5:00:00
 */
public class JarUtil {
	public static URLClassLoader loadJar(Thread thread, String... path) throws MalformedURLException {
		if (path.length < 1) {
			return null;
		}
		URL[] urls = new URL[path.length];
		for (int i = 0; i < path.length; i++) {
			urls[i] = new URL(path[i]);
		}
		URLClassLoader classLoader= new URLClassLoader(urls, thread.getContextClassLoader());
		return  classLoader;
	}
	
	public static InputStream getResourceAsStream(JarFile jar,String path) throws IOException{
		return jar.getInputStream(jar.getEntry(path));
	}
}
