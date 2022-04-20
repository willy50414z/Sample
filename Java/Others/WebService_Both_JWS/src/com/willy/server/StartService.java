package com.willy.server;

import javax.xml.ws.Endpoint;

public class StartService {

	public static void main(String[] args) {
		//建立連線
		String address = "http://10.32.85.100:8888/ns";
        Endpoint.publish(address, new MyServiceImpl());
        //建立webservice client，WSDL為http://localhost:8888/ns?wsdl
	}
}
