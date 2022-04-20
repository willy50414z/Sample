package com.willy.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public  class BeanFactory {
	private final static AnnotationConfigApplicationContext context;
	static {
		context = new AnnotationConfigApplicationContext(SpringConfig.class);
	}
	public static <T> T get(Class<T> cl) {
		return context.getBean(cl);
	}
}
