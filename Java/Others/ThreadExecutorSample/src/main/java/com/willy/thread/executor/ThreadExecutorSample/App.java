package com.willy.thread.executor.ThreadExecutorSample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App 
{
    public static void main( String[] args )
    {
        new FixedThreadPoolSample().exec();
    }
}
/**
 * newCacheThreadPool：有需要就建新執行續，有完成可以reuse的就reuse。
 * newFixThreadPool：執行緒 Pool 只會有固定數量的執行緒再執行，如果啟動的數量超過 pool 的數量那執行緒就會被放入 Queue 等待被執行。
 * newSingleThreadExecutor：執行緒 pool 只會有一個執行緒可以執行
 * newScheduledThreadPool：執行緒 pool 會按照排程去執行執行緒
 * newSingleThreadScheduledExecutor：執行緒 pool 只會有一個執行緒可以按照 scheduler 的排程去執行
 */
class CacheThreadPoolSample {
	public void exec() {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	    try {
	      for (int i = 0; i < 5; i++) {
	        Thread.sleep(1000);
	        cachedThreadPool.execute(new ThreadPoolMethod());
	      }
	    } catch(Exception e){
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
	    } catch(Exception e){
	      throw new RuntimeException(e);
	    } finally {
	    	fixedThreadPool.shutdown();
	    }
	}
}
class ThreadPoolMethod implements Runnable {
	public void run() {
		String threadName= Thread.currentThread().getName();
	    System.out.println("run " + threadName + " thread");
	}
}