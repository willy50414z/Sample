package com.willy.jms.tibco;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.tibco.tibjms.TibjmsConnectionFactory;

public class JMSUtil {
	private Session session = null;
	private TibjmsConnectionFactory factory;
	private MessageProducer producer = null;
	private TextMessage txtMsg = null;
	private Connection connection = null;
	private Queue requestQueue;
	private Queue replyQueue = null;
	
	private String initialContextUrl
		, principal
		, credentials
		, destinationName
		, replyDestinationName;
	
	public JMSUtil(String initialContextUrl, String principal, String credentials, String destinationName,
			String replyDestinationName) {
		super();
		this.initialContextUrl = initialContextUrl;
		this.principal = principal;
		this.credentials = credentials;
		this.destinationName = destinationName;
		this.replyDestinationName = replyDestinationName;
	}

	public String sendRecv(String msg) {
		TextMessage txtReplyMsg = null;
		String result = "";
		//Init
		try {
		factory = new TibjmsConnectionFactory(initialContextUrl);
		connection = factory.createConnection(principal, credentials);
		session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		requestQueue = session.createQueue(destinationName);
		replyQueue = session.createQueue(replyDestinationName);
		
		//Send
		MessageProducer msgProducer = session.createProducer(requestQueue);
		connection.start();
		txtMsg = session.createTextMessage();
		txtMsg.setJMSReplyTo(replyQueue);
		txtMsg.setText(msg);
		msgProducer.send(txtMsg);
		
		//Recv
		MessageConsumer msgConsumer = session.createConsumer(replyQueue,"JMSCorrelationID='"+txtMsg.getJMSMessageID()+"'");
		connection.start();
		Message msgg = msgConsumer.receive(3000);
		if(msgg!=null) {
			txtReplyMsg = (TextMessage) msgg;
			result = txtReplyMsg.getText();
		}else {
			result="";
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				session.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
