package com.willy.ct.mock;

public class DataSummaryDao {
	public int getResult(int result) throws Exception {
		doSomthing();
		return result;
	}
	public void doSomthing() {
		System.out.println("doSomthing..........");
	}
}
