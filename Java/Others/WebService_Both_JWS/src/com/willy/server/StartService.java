package com.willy.server;

import javax.xml.ws.Endpoint;

public class StartService {

	public static void main(String[] args) {
		//�إ߳s�u
		String address = "http://10.32.85.100:8888/ns";
        Endpoint.publish(address, new MyServiceImpl());
        //�إ�webservice client�AWSDL��http://localhost:8888/ns?wsdl
	}
}
