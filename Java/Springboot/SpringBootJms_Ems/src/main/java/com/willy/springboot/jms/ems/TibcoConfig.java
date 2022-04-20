package com.willy.springboot.jms.ems;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.tibco.tibjms.TibjmsConnectionFactory;

@Component
public class TibcoConfig {
	@Bean
	public TibjmsConnectionFactory getTibjmsConnectionFactory() throws JMSException {
		TibjmsConnectionFactory jmsConnectionFactory = new TibjmsConnectionFactory();
		jmsConnectionFactory.setServerUrl("tcp://172.24.16.70:27222");
		jmsConnectionFactory.setUserName("aacpsuat01");
		jmsConnectionFactory.setUserPassword("aacpsuat0160");
		return jmsConnectionFactory;
	}
	@Bean
	public JmsTemplate getJmsTemplate(@Autowired TibjmsConnectionFactory jmsConnectionFactory) {
		JmsTemplate jmsTempl = new JmsTemplate();
		jmsTempl.setConnectionFactory(jmsConnectionFactory);
		jmsTempl.setDefaultDestinationName("CTCB.ESB.UAT.Public.Service.Request.CPS");
		jmsTempl.setExplicitQosEnabled(true);
		jmsTempl.setDeliveryMode(2);
		jmsTempl.setSessionAcknowledgeModeName("CLIENT_ACKNOWLEDGE");
		jmsTempl.setSessionTransacted(true);
		return jmsTempl;
	}
}
