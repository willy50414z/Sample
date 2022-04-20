package com.javahonk.messaging;

import java.util.Date;

import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;


public class JavaTibcoPublisher {
	
	public static void main(String[] args) throws Exception {
		JMSUtil jms = new JMSUtil();
//		String[] ids = {"47217677","30986336","84429175","84429175","84429175","84429175","84429175","84429175","12358067","23988289","23988289","97486492","11456006","510217","59948971","300256","59948971","16159928","11456006","84429175","800182","12358067","12358067","12358067","11456006","12358067","500703","12358067","11456006","12358067","11456006","12358067","97486492","500703","500703","500703","500703","16159928","12358067","12358067","500703","86517510","86517510","86517510","84429175","86517510","84429175","84429175","30986336","30986336","84429175","86517510","86517510","800182","510217","500703","11456006","000095","97486492","30986336","30986336","LE000047","47217677","59948971","59948971","12358067","16159928","47217677","12358067","500703","11456006","100883","060818","500703","00655437","12358067","100883","12358067","84429175","500703","500703","510217","12358067","500703","12358067","500703","500703","30986336","800182","86517510","86517510","86517510","12358067","110327","84429175","84429175","303363","110327","80501224","300971","300971","100565","500703","12358067","510217","86517510","47217677","97486492","LE000047","11456006","500320","23988289","300256","300256","11456006","000095","12358067","30986336","12358067","LE000021","117322","117322","12358067","12358067","100883","500703","060818","12358067","500703","12358067","30986336","97486492","97486492","500703","100883","500703","12358067","86517510","110327","86517510","84429175","303363","84429175","12358067","30986336","59948971","300256","PO000175","47217677","23988289","300256","97486492","84429175","500320","97486492","500703","AC000013","113767","11456006","500320","23988289","LE000021","11456006","100883","11456006","12358067","303363","800182","12358067","12358067","11456006","12358067","100883","12358067","12358067","16159928","12358067","12358067","12358067","12358067","500703","97486492","12358067","500703","12358067","500703","500703","800182","12358067","30986336","86517510","84429175","500703","84429175","500703","84429175","100885","500703","12358067","97486492","11456006","11456006","11456006","12358067","16159928","30986336","11456006","23988289","59948971","500320","11456006","500320","510217","59948971","12358067","24688866","11456006","PO000175","47217677","303363","100883","12358067","117322","00657771","100883","00657771","060818","12358067","500703","500703","500703","500703","500703","500703","500703","86517510","500703","86517510","84429175","500703","84429175","84429175","500703","12358067","100565","59948971","47217677","30986336","86517510","86517510","12358067","11456006","86517510","59948971","000095","23988289","11456006","300256","84429175","300256","11456006","12358067","LE000047","12358067","59948971","11456006","11456006","86517510","12358067","12358067","11456006","500752","00657771","12358067","104712","16159928","47217677","500703","27567788","500703","500703","500703","500703","500703","500703","47217677","86517510","12358067","84429175","84429175","84429175","110327","84429175","12358067","84429175","12358067","84429175","12358067","12358067","12358067","AC000013","LE000047","97486492","LE000047","59948971","12358067","11456006","12358067  1","117322","12358067","11456006","117322","12358067","100883","12358067","12358067","500752","800182","500320","59948971","12358067","500703","36068968","12358067","100883","11456006","12358067","12358067","12358067","500703","500703","86517510","12358067","84429175","84429175","86517510","110327","86517510","84429175","80501224","84429175","000266","500703","100565","59948971","12358067","11456006","84429175","11456006","12358067","86517510","47217677","11456006","12358067","12358067","11456006","12358067","015063","12358067","86517510","12358067","500703","12358067","86517510","500703","86517510","30986336","86517510","86517510"};
//		String[] accts = {"163540120484","107540306222","90713000385037","107540399679","607001","906101003880 ","904133033639","905101008831","901540236080","141540110429","14154011","107131237409","901540154968","904335102177","107131237409","904335102177","107540355286","901540264586","120530006658","141131003246","11111111","50073","635540022971","657540017524","9041234567800","141540110403","2222","11111011 ","495540214233","11111011","901540248748","646540020988","107540357019","904303003318","111111","904135009713","904330003365","141540176418","904343002560","107131006025-","904105102174","163540120484","107131006025","646540020991","1009103100000258","107540399679","1008603100000258","824352000266","107540355260","901331000030","901540154968","901119907737","901131194133","904178001820","904305102178","107131148187","141540110403","244141000592","141540110429","107540306222","901131194133","244141000806","163540120484","015549999997","107540355655","107540332101","107131006252","200131001943","901540251049","901540154968","901540155048","90510000020674","902350608182","904343002560","107540357019","904399000008","905101008832","901131021646","60818","1000301000325","904133022378","12345678","901540252873TWD","901540248748","1009103100000009","904130003361","904133033639","107546000700","904178001820-","495890007132","495890000977","495800000977","141540176418","904101103274","902341019450","1008683100000258","904133033639","904105007037","82435000000139","904133009715","1009103100000258","902101005659","904305007037","495890000977","904105102174","901540154968","901131015872","107540355286","244141001070","495350004947","904135003201","107540355260","904343002560","904203002561","141131001345","904130000953","107131006025","901540231289","901350008945","24141000589","244141001096","904135007524","901540154968 ","904135007524","9051000002","1911151702074d72","234567890123","200540139493","904399000008","901131016211","901540248748","015118424000","635540022971","904103022340","90510000020675","244141000932 ","6465400209","46540020991","90410000166683","902109029996","902101019454","904130000953","904101103274","107540399844 ","163540119495","107131237412","904133002563","901141000301","015118424000","141540110403","107540355260","107540355260","141131003275","904305102178","904130000953","904133002563","495131010804","904331137670","163141000220","901131015872","901540231289","244141000589","904399000008","9041234567800","800182","107540108585","904105007033","47217677","640970021079","901350008631","LE000021","1","905341008837","646540020991","680540010268","901540147030","495131007930","49530006194","107540400788","9015401549680","107131006025","107540357035","111111","904135006787","1009101400000009","904133003313","904343002563","904305007037","901540154888","904130000953","244141000589","1009101000000315","107540399679","906101003880","824352000266","141540175817","904101008858","901540155048","1901540154968","120530006658","495131007930"," 495350004947","141540110429","392540106525","904305102178","90133100030","904178001820","107540355286","107540355273","244141000589","107540306222","244141000592","904350000018","163540119495","107540399679","901540321115","107131237577","901141002066","901540154968","904203033637","905101008831","904305007037","643157739981","107540357019","905101008830","107540357527","902350608181","B0004R","904135003201","141540176418","904303002568","904135132664","646540020991","904303022373","904133002560","904305007037","904178001820","244141000932","90713300386037","904105007033","90210000065421","904101003882","904111003885","901540154968","902361005652","107540355260","901540155035","901331000030","495131010804","901540155035","107131237412","107540332102","901540315370","141540110403","904305102178","141540110416","904168001827","141540110403","141540176007","904101214626","163540119495","107546011359","495131008159","107131006258","10540355260","107540355286","901540231289","107540355260","1111","1075401018585","400885842500","904178001820","646540020991","107131012158","904101047123","901540264573","901540315370","9051234567800","495540411654","1111111","1111","111111111","904303022344","904135133168","904105007033-","47217677","4958900009770","111111111111111","904101191309","90713000385000","1008683100600014","904130000953","608181","200540137660","906101005820","901540155035","902109029996","901540154968","901540155048","107540332102","244141000932","244141001096","107131237412","2144141001096","904168001827","107549000082","901141540110429","901540213469","904101173220","901540248748","10754035526","244141000592","107540320949","20052","904105007033","2244141000657","904135007524","904338001822","244141000593","901540231289","1009101000000009","904305007037USD","107540400856","1000301000325","904399000008","901543168168","400885842500 ","901540252873","901540236093","12345678","904133031194","901119907738","904133003834","906101005875","906331003883","904101103274","904105007033","608181","15046111","824352000266","905101001634","824352000266","904105007034","902361005651","901540320624","107540399844","901141000301","904133018777","107540355260","107131006252","107540400788","901131194832","11111011","400885842500","901543168168","163141000107","679540016930","15063101","12358067","495540381726","901131026227","904193022376","11111","141540176418","495540381726","901540236093","646540020991","901540236930","49589007132"};
//		Long txnId = 2020042702328648L;
		String responseMsg;
//		for(int i=0;i<ids.length;i++) {
//			txnId++;
//		String[] arg = {"LE000021-244141000673","12358067-200540137660","12358067-107540399844","12358067-107540399844 ","16159928-901540264560","89466640-107350000789","16085840-901131003855","84429175-141540175817","12358067-901350001748","86517510-107540399679","500703-904105007033","904399000008-904399000008","16159928-901540264560 ","47217677-904399000008","80027966-107540400953","47217677-200131001943","12358067-107546011359","12358067-141540176214"};
//		for(String aa : arg) {
			String txnId="FXML"+new Date().getTime();
			String msg = "<ser:ServiceEnvelope xmlns:ser=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceEnvelope\">\r\n" + 
					"	<ser1:ServiceHeader xmlns:ser1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceHeader\">\r\n" + 
					"		<ser1:StandardType>BSMF</ser1:StandardType>\r\n" + 
					"		<ser1:StandardVersion>01</ser1:StandardVersion>\r\n" + 
					"		<ser1:TrackingID>FXML022012617101</ser1:TrackingID>\r\n" + 
					"		<ser1:ServiceName>FXMLPmtAdd</ser1:ServiceName>\r\n" + 
					"		<ser1:ServiceVersion>01</ser1:ServiceVersion>\r\n" + 
					"		<ser1:SourceID>FXML</ser1:SourceID>\r\n" + 
					"		<ser1:ChannelID/>\r\n" + 
					"		<ser1:TransactionID>FXML022012617101</ser1:TransactionID>\r\n" + 
					"		<ser1:RqTimestamp>2022-01-26T17:13:15+08:00</ser1:RqTimestamp>\r\n" + 
					"	</ser1:ServiceHeader>\r\n" + 
					"	<ser1:ServiceBody xmlns:ser1=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/EMF/ServiceBody\">\r\n" + 
					"		<ns:FXMLPmtAddRq xmlns:ns=\"http://ns.chinatrust.com.tw/XSD/CTCB/ESB/Message/BSMF/FXMLPmtAddRq/01\">\r\n" + 
					"			<ns:TxnCode>FXMLPmtAdd</ns:TxnCode>\r\n" + 
					"			<ns:RqUID>FXML022012617100110</ns:RqUID>\r\n" + 
					"			<ns:TRAN_DATE>20220126</ns:TRAN_DATE>\r\n" + 
					"			<ns:TRAN_TIME>171315</ns:TRAN_TIME>\r\n" + 
					"			<ns:TRAN_CODE>100300</ns:TRAN_CODE>\r\n" + 
					"			<ns:XML_SEQ>2022-01-26 17:13:15.</ns:XML_SEQ>\r\n" + 
					"			<ns:APPOINT_DATE>20220126</ns:APPOINT_DATE>\r\n" + 
					"			<ns:PAY_BANK>873</ns:PAY_BANK>\r\n" + 
					"			<ns:PAY_BRANCH>3202</ns:PAY_BRANCH>\r\n" + 
					"			<ns:PAY_ACCT>0011002299998877</ns:PAY_ACCT>\r\n" + 
					"			<ns:PAY_NAME>財金資訊股份有限公司</ns:PAY_NAME>\r\n" + 
					"			<ns:PAY_ID>80102964</ns:PAY_ID>\r\n" + 
					"			<ns:RCV_BANK>822</ns:RCV_BANK>\r\n" + 
					"			<ns:RCV_BRANCH>0023</ns:RCV_BRANCH>\r\n" + 
					"			<ns:RCV_ACCT>16400100889992</ns:RCV_ACCT>\r\n" + 
					"			<ns:RCV_NAME>中文姓名34889</ns:RCV_NAME>\r\n" + 
					"			<ns:RCV_ID>16688928</ns:RCV_ID>\r\n" + 
					"			<ns:PAY_AMT>139</ns:PAY_AMT>\r\n" + 
					"			<ns:RESOURCE></ns:RESOURCE>\r\n" + 
					"		</ns:FXMLPmtAddRq>\r\n" + 
					"	</ser1:ServiceBody>\r\n" + 
					"</ser:ServiceEnvelope>";
			System.out.println("request = "+msg);
			responseMsg = jms.sendRecv(msg);
			System.out.println("response = "+responseMsg);
			
//			QueueConnection conn;
//			String serverUrl = "172.24.16.20:27222";
//		    String userName = "aaebuat01";
//		    String password = "aaebuat0160";
//		    QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(serverUrl);
//			conn = factory.createQueueConnection(userName,password);
//			conn.start();
//			QueueSession session = conn.createQueueSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
//			javax.jms.Queue queueOut = session.createQueue("CTCB.ESB.UAT.Public.Provider.Request.EvtFTP_AAEB");
//			QueueSender sender = session.createSender(queueOut);
//			javax.jms.TextMessage msgResponse = session.createTextMessage();
//            msgResponse.setJMSCorrelationID("EMS-SIT-EMS01.4401E461D005481810363:5118"); //設定 CorrelationID 
//            msgResponse.setText(msg);
//            sender.send(msgResponse);
	}

}