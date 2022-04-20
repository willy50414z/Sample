package com.willy.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class Testinterceptor extends AbstractInterceptor{
	public String getInterceptorkey() {
		return interceptorkey;
	}

	public void setInterceptorkey(String interceptorkey) {
		this.interceptorkey = interceptorkey;
	}
	private String interceptorkey;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		System.out.println("intercept start....");
		System.out.println("攔截器初始值 -- "+interceptorkey);
		String resultString=arg0.invoke();//傳給下一層interceptor
		System.out.println("intercept end....");
		return resultString;
	}
}
