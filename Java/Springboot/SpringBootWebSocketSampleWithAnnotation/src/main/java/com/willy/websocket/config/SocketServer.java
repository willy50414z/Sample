package com.willy.websocket.config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.willy.websocket.model.Client;

@Component
@ServerEndpoint(value = "/socketServer/{userName}")
public class SocketServer {

	private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

	private static CopyOnWriteArraySet<Client> clientSet = new CopyOnWriteArraySet<Client>();

	private Session session;

	//預設管理者帳號
	private final static String SYS_USERNAME = "niezhiliang9595";

	@OnOpen
	public void open(Session session, @PathParam(value = "userName") String userName) {
		this.session = session;
		clientSet.add(new Client(userName, session));
		logger.info("用戶端:【{}】連接成功", userName);
	}

	//client->admin
	@OnMessage
	public void onMessage(String message) {
		Client client = clientSet.stream().filter(cli -> cli.getSession() == session).collect(Collectors.toList()).get(0);
		sendMessage(client.getUserName() + "<--" + message, SYS_USERNAME);
		logger.info("用戶端:【{}】發送資訊:{}", client.getUserName(), message);
	}

	@OnClose
	public void onClose() {
		clientSet.forEach(client -> {
			if (client.getSession().getId().equals(session.getId())) {

				logger.info("用戶端:【{}】斷開連接", client.getUserName());
				clientSet.remove(client);

			}
		});
	}

	@OnError
	public void onError(Throwable error) {
		clientSet.forEach(client -> {
			if (client.getSession().getId().equals(session.getId())) {
				clientSet.remove(client);
				logger.error("用戶端:【{}】發生異常", client.getUserName());
				error.printStackTrace();
			}
		});
	}

	
	public synchronized static void sendMessage(String message, String userName) {
		//遍覽所有上線的User，找出Admin欲發送的User發信息
		clientSet.forEach(client -> {
			if (userName.equals(client.getUserName())) {
				try {
					client.getSession().getBasicRemote().sendText(message);
					logger.info("服務端推送給用戶端 :【{}】", client.getUserName(), message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	//線上Customer人數
	public synchronized static int getOnlineNum() {
		return clientSet.stream().filter(client -> !client.getUserName().equals(SYS_USERNAME))
				.collect(Collectors.toList()).size();
	}

	//線上Customer名稱
	public synchronized static List<String> getOnlineUsers() {
		List<String> onlineUsers = clientSet.stream().filter(client -> !client.getUserName().equals(SYS_USERNAME))
				.map(client -> client.getUserName()).collect(Collectors.toList());
		return onlineUsers;
	}

	public synchronized static void sendAll(String message) {
		// 群發，不能發送給服務端自己
		clientSet.stream().filter(cli -> cli.getUserName() != SYS_USERNAME).forEach(client -> {
			try {
				client.getSession().getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		logger.info("服務端推送給所有用戶端 :【{}】", message);
	}

	public synchronized static void SendMany(String message, String[] persons) {
		for (String userName : persons) {
			sendMessage(message, userName);
		}
	}
}
