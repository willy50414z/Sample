package com.willy.listener;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public class ESBListener {
	
	public static void main(String[] args) {
		new TibcoListener().start();
	}

}
