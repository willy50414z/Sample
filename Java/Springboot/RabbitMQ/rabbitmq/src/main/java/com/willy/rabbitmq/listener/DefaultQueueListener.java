package com.willy.rabbitmq.listener;

import com.willy.rabbitmq.config.Const;
import com.willy.rabbitmq.po.UserPO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class DefaultQueueListener
{
	@RabbitHandler
	@RabbitListener(queues = Const._DEFAULT_QUEUE_NAME)
	public void printMessageInQueue11(String message) {
		System.out.println("Receive message[" + message + "] from Queue[" + Const._DEFAULT_QUEUE_NAME + "]");
	}

	@RabbitHandler
	@RabbitListener(queues = Const._QUEUE_NAME_USER)
	public void printUserInQueue11(UserPO message) {
		System.out.println("Receive message[" + message + "] from Queue[" + Const._QUEUE_NAME_USER + "]");
	}

	@RabbitHandler
	@RabbitListener(queues = Const._QUEUE_NAME_21)
	public void printMessageInQueue21(String message) {
		System.out.println("Receive message[" + message + "] from Queue[" + Const._QUEUE_NAME_21 + "]");
	}

	@RabbitHandler
	@RabbitListener(queues = Const._QUEUE_NAME_32)
	public void printMessageInQueue32(String message) {
		System.out.println("Receive message[" + message + "] from Queue[" + Const._QUEUE_NAME_32 + "]");
	}

	@RabbitHandler
	@RabbitListener(queues = Const._QUEUE_NAME_42)
	public void printMessageInQueue42(String message) {
		System.out.println("Receive message[" + message + "] from Queue[" + Const._QUEUE_NAME_42 + "]");
	}
}
