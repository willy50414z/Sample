package com.willy.springboot.jms.ems;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	@Autowired
	private JmsTemplate jmsTemplate;
	private String message;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		message = "<?xml version=\"1.0\"?>\r\n" + 
				"<ns0:ServiceEnvelope xmlns:ns0=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceEnvelope\">\r\n" + 
				"   <ns1:ServiceHeader xmlns:ns1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceHeader\">\r\n" + 
				"     <ns1:StandardType>BSMF</ns1:StandardType>\r\n" + 
				"     <ns1:StandardVersion>01</ns1:StandardVersion>\r\n" + 
				"     <ns1:ServiceName>CIFSearch</ns1:ServiceName>\r\n" + 
				"     <ns1:ServiceVersion>01</ns1:ServiceVersion>\r\n" + 
				"     <ns1:SourceID>AACPS</ns1:SourceID>\r\n" + 
				"     <ns1:TransactionID>CPS200204582596</ns1:TransactionID>\r\n" + 
				"     <ns1:RqTimestamp>2020-02-04T14:13:51.689+08:00</ns1:RqTimestamp>\r\n" + 
				"   </ns1:ServiceHeader>\r\n" + 
				"   <ns1:ServiceBody xmlns:ns1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceBody\">\r\n" + 
				"     <CIFSearch xmlns=\"http://ns.chinatrust.com.tw/XSD/CTCB/CPS/Message/BSMF/CIFSearchRq/01\">\r\n" + 
				"       <CustPermId>00000001</CustPermId>\r\n" + 
				"       <CntryCode>TW</CntryCode>\r\n" + 
				"       <TxNo>CPS200204410867</TxNo>\r\n" + 
				"     </CIFSearch>\r\n" + 
				"   </ns1:ServiceBody>\r\n" + 
				" </ns0:ServiceEnvelope>";
		
		//發送
//		QueueConnection connection = null;
//        QueueConnectionFactory factory = new TibjmsQueueConnectionFactory("tcp://172.24.16.70:27222");
//        connection = factory.createQueueConnection("aacpsuat01", "aacpsuat0160");
//        
//        QueueSession session = connection.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
//        Queue receiverQueue = session.createQueue("CTCB.ESB.UAT.Public.Service.Request.C01");
//        
//        QueueSender sender = session.createSender(receiverQueue);
//        Message message = session.createTextMessage(this.message);
//        sender.send(message);
//        session.close();
//        connection.close();
//		jmsTemplate.send("CTCB.ESB.UAT.Public.Service.Reply.CPS",new MessageCreator() {
//			public Message createMessage(Session session) throws JMSException {
//				return session.createTextMessage(message);
//			}
//		});
//		
//		//接收
		Message msg = jmsTemplate.receive();//.receive("CTCB.ESB.UAT.Public.Service.Reply.CPS");
//		System.out.println(((TextMessage) msg).getText());
	}

}
