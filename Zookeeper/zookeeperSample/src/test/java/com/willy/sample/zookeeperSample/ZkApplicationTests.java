package com.willy.sample.zookeeperSample;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
public class ZkApplicationTests
{
	private ZooKeeper zooKeeper;

	@Before
	public void connect() throws IOException
	{
		zooKeeper = new ZooKeeper("127.0.0.1:2281,127.0.0.1:2282,127.0.0.1:2283", 500000,
				watchedEvent -> System.out.println("Receive watched event：" + watchedEvent));
	}

	@Test
	public void create() {
		try {
			listNodes();
			System.out.println("add node[\"path\"]");
			String nodeName = zooKeeper.create("/path", "value".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			System.out.println("create node["+nodeName+"] success");
			listNodes();
		} catch (KeeperException.NodeExistsException e) {
			// 节点存在
			System.out.println("节点已存在: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void delete() throws InterruptedException, KeeperException, IOException
	{
		zooKeeper.create("/customer", "value".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		listNodes();
		zooKeeper.delete("/customer", -1);
		listNodes();
	}

	@Test
	public void listNodes() throws InterruptedException, KeeperException, IOException
	{
		System.out.println("exist node list" + zooKeeper.getChildren("/", false));
	}

	@Test
	public void listenNodeList() throws InterruptedException, KeeperException, IOException
	{
		//start listen node list
		zooKeeper.getChildren("/", true);
		//after create new node, add event will be gotten by watcher
		zooKeeper.create("/list", "value".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		listNodes();
	}

	@Test
	public void listenNodeExist() throws InterruptedException, KeeperException, IOException
	{
		//start listen node list
		zooKeeper.exists("/list", true);
		//after create new node, add event will be gotten by watcher
		zooKeeper.create("/list", "value".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		listNodes();
	}



//	@Test
//	void create() {
//		try {
//			String node = zooKeeper.create("/path", "value".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			System.out.println("create node: " + node);
//		} catch (KeeperException.NodeExistsException e) {
//			// 节点存在
//			System.out.println("节点已存在: " + e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
