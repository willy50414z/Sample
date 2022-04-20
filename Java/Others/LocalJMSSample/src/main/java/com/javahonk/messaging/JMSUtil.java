package com.javahonk.messaging;

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
	private 	TibjmsConnectionFactory factory;
	private MessageProducer producer = null;
	private TextMessage txtMsg = null;
	private Connection connection = null;
	private Queue requestQueue;
	private Queue replyQueue = null;
	
	
	public String sendRecv(String msg) {
//		String initialContextUrl = "tcp://172.24.17.41:17222"
//				, principal = "bcicsit01"
//				, credentials = "bcicsit0141"
//				, destinationName = "CTCB.ESB.SIT.Public.Service.Request.C01"
//				, replyDestinationName = "CTCB.ESB.SIT.Public.Service.Reply.BCIC";
// 		String initialContextUrl = "tcp://172.24.16.20:27222"
//				, principal = "bcicuat01"
//				, credentials = "bcicuat0120"
//				, destinationName = "CTCB.ESB.UAT.Public.Service.Request.C01"
//				, replyDestinationName = "CTCB.ESB.UAT.Public.Service.Reply.BCIC";
 		String initialContextUrl = "tcp://172.24.17.41:17222"
				, principal = "fxmlsit01"
				, credentials = "fxmlsit0141"
				, destinationName = "CTCB.ESB.SIT.Public.Service.Request.C01"
				, replyDestinationName = "CTCB.ESB.SIT.Public.Service.Reply.FXML";
//		String initialContextUrl = "tcp://172.24.17.41:17222"
//				, principal = "aaebsit01"
//				, credentials = "aaebsit0159"
//				, destinationName = "CTCB.ESB.SIT.Public.Provider.Request.EvtFTP_AAEB"
//				, replyDestinationName = "CTCB.ESB.SIT.Public.Provider.Reply.EvtFTP_AAEB";
//		String initialContextUrl = "tcp://172.24.16.20:27222"
//				, principal = "aaebuat01"
//				, credentials = "aaebuat0160"
//				, destinationName = "CTCB.ESB.UAT.Public.Provider.Request.EvtFTP_AAEB"
//				, replyDestinationName = "CTCB.ESB.UAT.Public.Provider.Reply.EvtFTP_AAEB";
//		String initialContextUrl = "tcp://172.24.17.41:17222"
//		, principal = "aaebsit01"
//		, credentials = "aaebsit0159"
//		, destinationName = "CTCB.ESB.SIT.Public.Provider.Request.FCAT"
//		, replyDestinationName = "CTCB.ESB.SIT.Public.Provider.Reply.FCAT";
//		String initialContextUrl = "tcp://172.24.16.20:27222"
//				, principal = "aaebuat01"
//				, credentials = "aaebuat0160"
//				, destinationName = "CTCB.ESB.UAT.Public.Provider.Request.FCAT"
//				, replyDestinationName = "CTCB.UAT.SIT.Public.Provider.Reply.FCAT";
//		String initialContextUrl = "tcp://172.24.17.41:17222"
//				, principal = "bcicsit01"
//				, credentials = "bcicsit0141"
//				, destinationName = "CTCB.ESB.SIT.Public.Service.Request.C01"
//				, replyDestinationName = "CTCB.ESB.SIT.Public.Service.Reply.BCIC";
//		String initialContextUrl = "tcp://172.24.17.41:17222"
//		, principal = "bcicsit01"
//		, credentials = "bcicsit0141"
//		, destinationName = "CTCB.ESB.SIT.Public.Service.Request.C01"
//		, replyDestinationName = "CTCB.ESB.SIT.Public.Service.Reply.BCIC";
//		String initialContextUrl = "172.24.16.20:27222"
//		, principal = "hkibcoredbuat01"
//		, credentials = "hkibcoredbuat0160"
//		, destinationName = "CTCB.ESB.UAT.Public.Service.Request.C01"
//		, replyDestinationName = "";
		
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
		System.out.println("JMSMessageID = " + txtMsg.getJMSMessageID());
		//Recv
		if(replyDestinationName.length() != 0 ) {
			MessageConsumer msgConsumer = session.createConsumer(replyQueue,"JMSCorrelationID='"+txtMsg.getJMSMessageID()+"'");
			connection.start();
			Message msgg = msgConsumer.receive(3000);
			if(msgg!=null) {
				txtReplyMsg = (TextMessage) msgg;
				result = txtReplyMsg.getText();
			}else {
				result="";
			}
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
