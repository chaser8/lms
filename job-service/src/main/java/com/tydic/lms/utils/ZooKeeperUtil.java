/**   
 * @Title: CuratorUtils.java
 * @Package com.tydic.lms.utils
 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
 * @date 2017年2月16日 下午5:41:37
 * @version 
 */
package com.tydic.lms.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ZooKeeperUtil {
	private static final Logger log = Logger.getLogger(ZooKeeperUtil.class);

	private static CuratorFramework client;
	public static void initZkClient(String zkAddress) {
		client = CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000, 3));
		client.start();
	}

	/**
	 * 创建node
	 * 
	 * @param nodeName
	 * @param value
	 * @return
	 */
	public static boolean createNode(String nodeName, String value,CreateMode mode) {
		boolean suc = false;
		try {
			Stat stat = getClient().checkExists().forPath(nodeName);
			if(mode==null){
				mode=CreateMode.PERSISTENT;
			}
			if (stat == null) {
				String opResult = null;
				if (Strings.isNullOrEmpty(value)) {
					opResult = getClient().create().withMode(mode).forPath(nodeName);
				} else {
					opResult = getClient().create().withMode(mode).forPath(nodeName, value.getBytes(Charsets.UTF_8));
				}
				suc = Objects.equal(nodeName, opResult);
			}
		} catch (Exception e) {
			log.error("create node error!",e);
		}
		return suc;
	}

	/**
	 * 更新节点
	 * 
	 * @param nodeName
	 * @param value
	 * @return
	 */
	public static boolean updateNode(String nodeName, String value) {
		boolean suc = false;
		try {
			Stat stat = getClient().checkExists().forPath(nodeName);
			if (stat != null) {
				Stat opResult = getClient().setData().forPath(nodeName, value.getBytes(Charsets.UTF_8));
				suc = opResult != null;
			}
		} catch (Exception e) {
			log.error("update node error!",e);
		}
		return suc;
	}

	/**
	 * 删除节点
	 * 
	 * @param nodeName
	 */
	public static void deleteNode(String nodeName) {
		try {
			Stat stat = client.checkExists().forPath(nodeName);
			if(stat!=null)
			getClient().delete().deletingChildrenIfNeeded().forPath(nodeName);
		} catch (Exception e) {
			log.error("delete node error!",e);
		}
	}

	/**
	 * 找到指定节点下所有子节点的名称与值
	 * 
	 * @param node
	 * @return
	 */
	public static Map<String, String> listChildrenDetail(String node) {
		Map<String, String> map = Maps.newHashMap();
		try {
			GetChildrenBuilder childrenBuilder = getClient().getChildren();
			List<String> children = childrenBuilder.forPath(node);
			GetDataBuilder dataBuilder = getClient().getData();
			if (children != null) {
				for (String child : children) {
					String propPath = ZKPaths.makePath(node, child);
					map.put(child, new String(dataBuilder.forPath(propPath), Charsets.UTF_8));
				}
			}
		} catch (Exception e) {
			log.error("get children detail error!",e);
		}
		return map;
	}

	/**
	 * 列出子节点的名称
	 * 
	 * @param node
	 * @return
	 */
	public static List<String> listChildren(String node) {
		List<String> children = Lists.newArrayList();
		try {
			GetChildrenBuilder childrenBuilder = getClient().getChildren();
			children = childrenBuilder.forPath(node);
		} catch (Exception e) {
			log.error("get children error!",e);
		}
		return children;
	}

	/**
	 * 增加监听
	 * 
	 * @param node
	 * @param isSelf
	 *            true 为node本身增加监听 false 为node的子节点增加监听
	 * @throws Exception
	 */
	public static void addWatch(String node, boolean isSelf) throws Exception {
		if (isSelf) {
			getClient().getData().watched().forPath(node);
		} else {
			getClient().getChildren().watched().forPath(node);
		}
	}

	/**
	 * 增加监听
	 * 
	 * @param node
	 * @param isSelf
	 *            true 为node本身增加监听 false 为node的子节点增加监听
	 * @param watcher
	 * @throws Exception
	 */
	public static void addWatch(String node, boolean isSelf, Watcher watcher) throws Exception {
		if (isSelf) {
			getClient().getData().usingWatcher(watcher).forPath(node);
		} else {
			getClient().getChildren().usingWatcher(watcher).forPath(node);
		}
	}

	/**
	 * 增加监听
	 * 
	 * @param node
	 * @param isSelf
	 *            true 为node本身增加监听 false 为node的子节点增加监听
	 * @param watcher
	 * @throws Exception
	 */
	public static void addWatch(String node, boolean isSelf, CuratorWatcher watcher) throws Exception {
		if (isSelf) {
			getClient().getData().usingWatcher(watcher).forPath(node);
		} else {
			getClient().getChildren().usingWatcher(watcher).forPath(node);
		}
	}

	/**
	 * 销毁资源
	 */
	public static void destory() {
		if (client != null) {
			client.close();
		}
	}

	/**
	 * 获取client
	 * 
	 * @return
	 */
	public static CuratorFramework getClient() {
		return client;
	}

	public static void main(String[] args) throws Exception {
		ZooKeeperUtil.initZkClient("localhost:2181");
//		ZooKeeperUtil.createNode("/zkroot/test1", "你好abc11");
//		ZooKeeperUtil.createNode("/zkroot/test2", "你好abc22");
//		ZooKeeperUtil.updateNode("/zkroot/test2", "你好abc22");
		List<String> list = ZooKeeperUtil.listChildren("/zkroot");
		Map<String, String> map = ZooKeeperUtil.listChildrenDetail("/zkroot");
		// curator.deleteNode("/zkroot");
		// curator.destory();
		System.out.println("=========================================");
		for (String str : list) {
			System.out.println(str);
		}

		System.out.println("=========================================");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "=>" + entry.getValue());
		}

		// 增加监听
		ZooKeeperUtil.addWatch("/zkroot", false);
		TimeUnit.SECONDS.sleep(600);
	}

}