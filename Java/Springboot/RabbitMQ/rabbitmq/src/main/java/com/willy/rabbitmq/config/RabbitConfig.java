package com.willy.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig
{
	@Bean
	public Queue getQueue11() {
		return new Queue(Const._DEFAULT_QUEUE_NAME);
	}

	@Bean
	public Queue getQueue21() {
		return new Queue(Const._QUEUE_NAME_21);
	}
	@Bean
	public Queue getQueue32() {
		return new Queue(Const._QUEUE_NAME_32);
	}
	@Bean
	public Queue getQueue42() {
		return new Queue(Const._QUEUE_NAME_42);
	}

	@Bean
	FanoutExchange fanoutExchange() {
		return new FanoutExchange(Const._EXCHANGE_NAME_FANOUT);
	}

	@Bean
	DirectExchange directExchange() {
		return new DirectExchange(Const._EXCHANGE_NAME_DIRECT);
	}

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange(Const._EXCHANGE_NAME_TOPIC);
	}

	@Bean
	HeadersExchange headersExchange() {
		return new HeadersExchange(Const._EXCHANGE_NAME_HEADERS);
	}

	//bind queue to exchange
	//DirectExchange
	@Bean
	Binding bindDirect11(Queue getQueue11, DirectExchange directExchange){
		return BindingBuilder.bind(getQueue11).to(directExchange).with("routeKey.11");
	}
	@Bean
	Binding bindDirect21(Queue getQueue21, DirectExchange directExchange){
		return BindingBuilder.bind(getQueue21).to(directExchange).with("routeKey.21");
	}

	//TopicExchange
	@Bean
	Binding bindTopic11(Queue getQueue11, TopicExchange topicExchange){
		return BindingBuilder.bind(getQueue11).to(topicExchange).with("routeKey.*");
	}

	@Bean
	Binding bindTopic21(Queue getQueue21, TopicExchange topicExchange){
		return BindingBuilder.bind(getQueue21).to(topicExchange).with("routeKey.*");
	}

	@Bean
	Binding bindTopic32(Queue getQueue32, TopicExchange topicExchange){
		return BindingBuilder.bind(getQueue32).to(topicExchange).with("routeKey.#");
	}

	@Bean
	Binding bindTopic42(Queue getQueue42, TopicExchange topicExchange){
		return BindingBuilder.bind(getQueue42).to(topicExchange).with("routeKey.#");
	}


	//FanoutExchange
	@Bean
	Binding bindingExchange42(Queue getQueue42,FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(getQueue42).to(fanoutExchange);
	}
	@Bean
	Binding bindingExchange11(Queue getQueue11,FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(getQueue11).to(fanoutExchange);
	}
	@Bean
	Binding bindingExchange21(Queue getQueue21,FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(getQueue21).to(fanoutExchange);
	}

}
