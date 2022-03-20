package com.willy.thread.executor.ThreadExecutorSample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
	/**
	 * 	執行續實現方式 
	 * 	1. 繼承Thread(底層同樣是實現Runnable) new一個Instance只能run一次 
	 * 	2.實現Runnable
	 * 	3.實現callable (會回傳Future物件，可取得回傳值)
	 * 
	 * 	實現框架
	 * 	TreadPoolExecutor
	 * 		CacheThreadPool：有需要就建新執行續，有完成可以reuse的就reuse。
	 * 		FixThreadPool：執行緒 Pool只會有固定數量的執行緒再執行，如果啟動的數量超過 pool 的數量那執行緒就會被放入 Queue 等待被執行。
	 * 		SingleThreadExecutor：執行緒 pool 只會有一個執行緒可以執行
	 * 
	 *  ScheduledThreadPoolExecutor
	 *  	SingleThreadScheduledExecutor：執行緒 pool 只會有一個執行緒可以按照 scheduler的排程去執行
	 *  	ScheduledThreadPoolExecutor: 多個執行緒，週期性執行
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// 繼承Thread
		ExtendThreadSample s = null;
		for (int i = 0; i < 10; i++) {
			s = new ExtendThreadSample();
			s.start();
		}
		try {
			s.start();
		} catch (IllegalThreadStateException e) {
			System.err.println("繼承Thread的class只能run start一次");
		}
	
		// 實現Runnable
		Thread t = null;
		for (int i = 0; i < 10; i++) {
			t = new Thread(new ImplementRunnableSample());
			t.start();
		}
		try {
			t.start();
		} catch (IllegalThreadStateException e) {
			System.err.println("實現Runnable的class只能run start一次");
		}
	
		// 實現callable
		List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
	
		for (int i = 0; i < 3; i++) {
			FutureTask<Integer> futureTask = new FutureTask<Integer>(new ImplementCallableSample());
			taskList.add(futureTask);
		}
	
		for (FutureTask<Integer> f : taskList) {
			new Thread(f).start();
		}
	
		Thread.sleep(3000);
		for (FutureTask<Integer> f : taskList) {
			if (f.isDone()) {
				System.out.printf("我是第%s次執行的！\n", f.get());
			}
		}
		
		//TreadPoolExecutor - CacheThreadPool
	    new CacheThreadPoolSample().exec();
	    
	    //TreadPoolExecutor - FixThreadPool
	    new FixedThreadPoolSample().exec();
	    
	    //TreadPoolExecutor - SingleThreadExecutor
	    new SingleThreadExecutorSample().exec();
        
		//ScheduledThreadPoolExecutor - ScheduledThreadExecutor
		new ScheduledThreadExecutorSample().exec();
		    
		//ScheduledThreadPoolExecutor - SingleScheduledThreadExecutor
		new SingleScheduledThreadExecutorSample().exec();
	}
}

class ExtendThreadSample extends Thread {
	@Override
	public void run() {
		System.out.println("run Thread " + Thread.currentThread().getName() + " thread");
	}
}

class ImplementRunnableSample implements Runnable {
	public void run() {
		System.out.println("run Runnable " + Thread.currentThread().getName() + " thread");
	}
}

class ImplementCallableSample implements Callable<Integer> {
	private volatile static int count = 0;

	public Integer call() throws Exception {
		System.out.println("run Callable " + Thread.currentThread().getName() + " thread");
		count++;
		return count;
	}
}

class CacheThreadPoolSample {
	public void exec() {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		try {
			// 同個thread，可reuse
			for (int i = 0; i < 5; i++) {
				Thread.sleep(1000);
				cachedThreadPool.execute(new ThreadPoolMethod());
			}
			System.out.println("---------Sample2---------");
			for (int i = 0; i < 30; i++) {
				cachedThreadPool.execute(new ThreadPoolMethod());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			cachedThreadPool.shutdown();
		}
	}
}

class FixedThreadPoolSample {
	public void exec() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		try {
			for (int i = 0; i < 30; i++) {
				fixedThreadPool.execute(new ThreadPoolMethod());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			fixedThreadPool.shutdown();
		}
	}
}


class SingleThreadExecutorSample {
	public void exec() {
		ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
		try {
			for (int i = 0; i < 5; i++) {
				singleThreadPool.execute(new ThreadPoolMethod());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			singleThreadPool.shutdown();
		}
	}
}

class ScheduledThreadExecutorSample {
	public void exec() {
		ScheduledExecutorService schedulerThreadPool = Executors.newScheduledThreadPool(5);
		try {
			 for (int i = 0; i < 3; i++) {
				 ThreadPoolMethod worker = new ThreadPoolMethod();
	            // 只執行一次，觸發後等5秒
//				schedulerThreadPool.schedule(worker, 5, TimeUnit.SECONDS);
	            //週期性執行，每5秒執行一次
				schedulerThreadPool.scheduleAtFixedRate(worker, 0, 5, TimeUnit.SECONDS);
	        }
			 Thread.sleep(10000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			schedulerThreadPool.shutdown();
		}
	}
}

class SingleScheduledThreadExecutorSample {
	public void exec() {
		ScheduledExecutorService singleSchedulerThreadPool = Executors.newSingleThreadScheduledExecutor();
		try {
			 for (int i = 0; i < 3; i++) {
				 ThreadPoolMethod worker = new ThreadPoolMethod();
	            // 只執行一次，觸發後等5秒
//				schedulerThreadPool.schedule(worker, 5, TimeUnit.SECONDS);
	            //週期性執行，每5秒執行一次
				singleSchedulerThreadPool.scheduleAtFixedRate(worker, 0, 5, TimeUnit.SECONDS);
	        }
			 Thread.sleep(10000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			singleSchedulerThreadPool.shutdown();
		}
	}
}

class ThreadPoolMethod implements Runnable {
	public void run() {
		String threadName = Thread.currentThread().getName();
		System.out.println("run " + threadName + " thread");
	}
}