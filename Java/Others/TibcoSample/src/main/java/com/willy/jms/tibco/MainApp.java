package com.willy.jms.tibco;

public class MainApp {

	public static void main(String[] args) throws InterruptedException {
		//設置參數
		String initialContextUrl = "tcp://172.24.16.70:27222"
				, principal = "bcicuat01"
				, credentials = "bcicuat0120"
				, destinationName = "CTCB.ESB.UAT.Public.Service.Request.C01"
				, replyDestinationName = "CTCB.ESB.UAT.Public.Service.Reply.BCIC";
		JMSUtil jms = new JMSUtil(initialContextUrl, principal, credentials, destinationName, replyDestinationName);
		String sendMsg = "<ser:ServiceEnvelope xmlns:ser=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceEnvelope\"><ser1:ServiceHeader xmlns:ser1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceHeader\"><ser1:StandardType>BSMF</ser1:StandardType><ser1:StandardVersion>01</ser1:StandardVersion><ser1:ServiceName>ChanCustInfoInq</ser1:ServiceName><ser1:ServiceVersion>01</ser1:ServiceVersion><ser1:SourceID>BCIC</ser1:SourceID><ser1:TransactionID>BCIC2005110000264013</ser1:TransactionID><ser1:RqTimestamp>2020-05-11T19:34:14.968+08:00</ser1:RqTimestamp></ser1:ServiceHeader><ser1:ServiceBody xmlns:ser1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceBody\"><ns:CustInqRq xmlns:ns=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/BSMF/CustInq/01\"><ns1:TxnCode xmlns:ns1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/BSMF/CTCBExt/01\">ChanCustInfoInq</ns1:TxnCode><ns1:RqUID xmlns:ns1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/BSMF/CTCBCore/01\">BCIC2005110000264013</ns1:RqUID><ns1:CustId xmlns:ns1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/BSMF/CTCBCore/01\"><ns1:SPName>CTCB</ns1:SPName><ns1:CustPermId>30986336</ns1:CustPermId><ns1:CustLoginId>AP2AP002</ns1:CustLoginId></ns1:CustId><ns1:Country xmlns:ns1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/BSMF/CTCBExt/01\">TWN</ns1:Country><ns1:ChanType xmlns:ns1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/BSMF/CTCBExt/01\">BCIC</ns1:ChanType></ns:CustInqRq></ser1:ServiceBody></ser:ServiceEnvelope>";
		String responseMsg = jms.sendRecv(sendMsg);
		System.out.println(responseMsg);
	}

}
