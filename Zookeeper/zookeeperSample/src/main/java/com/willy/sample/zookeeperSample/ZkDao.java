package com.willy.sample.zookeeperSample;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


@Component
public class ZkDao implements Watcher
{
	ZooKeeper zooKeeper;
	public ZkDao() throws IOException
	{
		zooKeeper = new ZooKeeper("127.0.0.1:2292", 500000, this);
	}

	/**
	 * 持久節點 persistent node
	 * 持久順序節點 persistent sequental
	 * 臨時節點 ephemeral node
	 * 臨時順序節點 ephemeral sequental
	 */
	//add
	public void add(String path, String value){
		try {
			String node = zooKeeper.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("create node: " + node);
		} catch (KeeperException.NodeExistsException e) {
			// 节点存在
			System.out.println("节点已存在: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//delete

	//update

	//query


	@Override
	public void process(WatchedEvent event)
	{
		CountDownLatch latch = new CountDownLatch(1);
		System.out.println("Receive watched event：" + event);
		if (Event.KeeperState.SyncConnected == event.getState()) {
			latch.countDown();
		}
	}
}
