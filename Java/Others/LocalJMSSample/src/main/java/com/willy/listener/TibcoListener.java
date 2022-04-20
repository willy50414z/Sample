package com.willy.listener;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.tibco.tibjms.TibjmsConnectionFactory;

public class TibcoListener implements MessageListener {
	private Session session = null;
	private TibjmsConnectionFactory factory;
	private MessageProducer producer = null;
	private TextMessage txtMsg = null;
	private Connection connection = null;
	private Queue requestQueue;
	private Queue replyQueue = null;

	String initialContextUrl = "tcp://172.24.17.41:17222", principal = "insksit01", credentials = "insksit0159",
			destinationName = "CTCB.ESB.SIT.Public.Provider.Request.INSK",
			replyDestinationName = "CTCB.ESB.SIT.Public.Provider.Reply.INSK";
	// String initialContextUrl = "tcp://172.24.16.70:27222"
	// , principal = "bcicuat01"
	// , credentials = "bcicuat0120"
	// , destinationName = "CTCB.ESB.UAT.Public.Service.Request.C01"
	// , replyDestinationName = "CTCB.ESB.UAT.Public.Service.Reply.BCIC";
	// String initialContextUrl = "tcp://172.24.16.20:27222"
	// , principal = "bcicuat01"
	// , credentials = "bcicuat0120"
	// , destinationName = "CTCB.ESB.UAT.Public.Service.Request.C01"
	// , replyDestinationName = "CTCB.ESB.UAT.Public.Service.Reply.BCIC";

	public void start() {
		try {
			factory = new TibjmsConnectionFactory(initialContextUrl);
			connection = factory.createConnection(principal, credentials);
			session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			replyQueue = session.createQueue(replyDestinationName);
			MessageConsumer QueueReceiver = session.createConsumer(replyQueue);
			QueueReceiver.setMessageListener(this);
			connection.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		String result;
		if(message!=null) {
			TextMessage txtReplyMsg = (TextMessage) message;
			try {
				result = txtReplyMsg.getText();
				System.out.println(result);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			result="";
		}
	}
}
