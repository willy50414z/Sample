package com.willy.thread.executor.ThreadExecutorSample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestApp {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
		Processor p = null;
		List<Future<Integer>> fList = new ArrayList<>();
		for(int i=0;i<100;i++) {
			p = new Processor();
			p.setSid(i);
			p.setTradeDate(new Date());
			fList.add(fixedThreadPool.submit(p));
		}
		Thread.sleep(7000);
		System.out.println("pp count = " + p.getPp().acctBalance);
//		Thread.sleep(3000);
//		for(int i=0;i<50;i++) {
//			System.out.println(fList.stream().filter(f -> f.isDone()).count() + " - " + fList.size());
//			Thread.sleep(1000);
//		}
	}

}
class ProssorFather{
	private int sid;
	private Date tradeDate;
	protected static ProfitPo pp = new ProfitPo();
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public static synchronized void addCount() {
		pp.acctBalance++;
		System.out.println(pp.acctBalance);
	}
	public ProfitPo getPp() {
		return pp;
	}
	public static void setPp(ProfitPo pp) {
		ProssorFather.pp = pp;
	}
	
}
class ProfitPo{
	int acctBalance;
}
class Processor extends ProssorFather implements Callable<Integer>{

	public Integer call() throws Exception {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		addCount();
		synchronized(pp) {
			pp.acctBalance++;
		}
		
//		System.out.println(Thread.currentThread().getName()+" - "+this.getSid()+" - "+this.getTradeDate());
		return 1;
	}
}