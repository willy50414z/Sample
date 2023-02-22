package com.willy.rabbitmq;

import com.willy.rabbitmq.config.Const;
import com.willy.rabbitmq.po.UserPO;
import com.willy.rabbitmq.service.MQService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RabbitmqApplicationTests {

	@Autowired
	private MQService mqSvc;

	@Test
	public void basicSendAndListen() {
		mqSvc.send("basicSendAndListen");
	}

	@Test
	public void objectMessage() {
		UserPO user = new UserPO();
		user.setName("Willy");
		user.setAge(18);
		mqSvc.send(Const._QUEUE_NAME_USER, user);
	}

	@Test
	public void sendMessageToDirect11Exchange() {
		mqSvc.send(Const._EXCHANGE_NAME_DIRECT, "routeKey.11", "Direct exchange 11 message");
	}

	@Test
	public void sendMessageToDirect21Exchange() {
		mqSvc.send(Const._EXCHANGE_NAME_DIRECT, "routeKey.21", "Direct exchange 21 message");
	}

	@Test
	public void sendOneCharKeyMessageToTopicExchange() {
		mqSvc.send(Const._EXCHANGE_NAME_TOPIC, "routeKey.char1", "Topic Exchange one char");
	}

	@Test
	public void sendMutiCharKeyMessageToTopicA2Exchange() {
		mqSvc.send(Const._EXCHANGE_NAME_TOPIC, "routeKey.char1.char2", "Topic Exchange muti char");
	}

	@Test
	public void sendMessageToFanoutExchange() {
		mqSvc.send(Const._EXCHANGE_NAME_FANOUT, "", "exchangeMessage");
	}

}
