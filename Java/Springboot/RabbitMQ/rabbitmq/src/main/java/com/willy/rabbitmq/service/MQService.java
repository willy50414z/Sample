package com.willy.rabbitmq.service;

import com.willy.rabbitmq.config.Const;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQService
{
	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(Object message) {
		System.out.println("Send message : " + message);
		this.rabbitTemplate.convertAndSend(Const._DEFAULT_QUEUE_NAME, message);
	}

	public void send(String queueName, Object message) {
		System.out.println("Send message : " + message);
		this.rabbitTemplate.convertAndSend(queueName, message);
	}

	public void send(String queueName, String routeKey, Object message) {
		System.out.println("Send message[" + message + "] to queue[" + queueName + "] with route key[" + routeKey + "]");
		this.rabbitTemplate.convertAndSend(queueName, routeKey, message);
	}
}
