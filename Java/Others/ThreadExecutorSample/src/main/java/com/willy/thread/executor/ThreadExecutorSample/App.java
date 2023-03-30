package com.willy.thread.executor.ThreadExecutorSample;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class App {
	/**
	 * 	執行續實現方式 
	 * 	1. 繼承Thread(底層同樣是實現Runnable) new一個Instance只能run一次 
	 * 	2.實現Runnable
	 * 	3.實現callable (會回傳Future物件，可取得回傳值)
	 * 
	 * 	實現框架 (主執行緒會等多執行緒都執行完才繼續)
	 * 	ThreadPoolExecutor
	 * 		CacheThreadPool：有需要就建新執行續，有完成可以reuse的就reuse。
	 * 		FixThreadPool：執行緒 Pool只會有固定數量的執行緒再執行，如果啟動的數量超過 pool 的數量那執行緒就會被放入 Queue 等待被執行。
	 * 		SingleThreadExecutor：執行緒 pool 只會有一個執行緒可以執行
	 * 
	 *  ScheduledThreadPoolExecutor
	 *  	SingleThreadScheduledExecutor：執行緒 pool 只會有一個執行緒可以按照 scheduler的排程去執行
	 *  	ScheduledThreadPoolExecutor: 多個執行緒，週期性執行
	 *  
	 *  Volatile關鍵字使變數在多個執行緒間可見,強制執行緒從主記憶體中讀取。
	 *  	1. 可見性: 多執行序間可以調用同個變數
	 *  	2. 非原子性: 非Thread Safe
	 *  
	 *  volatile 與 synchronized 的比較
	 *  	1. volatile輕量級，只能修飾變數。synchronized重量級，還可修飾方法
	 *  	2. volatile只能保證資料的可見性，不能用來同步，因為多個執行緒併發訪問volatile修飾的變數不會阻塞。
	 *			synchronized不僅保證可見性，而且還保證原子性，多個執行緒爭搶synchronized鎖物件時，會出現阻塞。
	 *  	
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		//lambdas
//		new Thread(() -> { // Lambda Expression
//			for(int i=1; i <= 5; i++) {
//				System.out.println("Child Thread: "+ i);
//				try {
//					Thread.sleep(500);
//				} catch(Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();

//		// 繼承Thread
//		ExtendThreadSample s = null;
//		for (int i = 0; i < 10; i++) {
//			s = new ExtendThreadSample();
//			s.start();
//		}
//		try {
//			s.start();
//		} catch (IllegalThreadStateException e) {
//			System.err.println("繼承Thread的class只能run start一次");
//		}
//
//		// 實現Runnable
//		Thread t = null;
//		ImplementRunnableSample is = new ImplementRunnableSample();
//		for (int i = 0; i < 10; i++) {
//			t = new Thread(is);
//			t.start();
//		}
//		try {
//			t.start();
//		} catch (IllegalThreadStateException e) {
//			System.err.println("實現Runnable - 實現Runnable的class只能run start一次");
//		}
//
//		// 實現callable
//		List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
//		for (int i = 0; i < 3; i++) {
//			FutureTask<Integer> futureTask = new FutureTask<Integer>(new ImplementCallableSample());
//			taskList.add(futureTask);
//		}
//		for (FutureTask<Integer> f : taskList) {
//			new Thread(f).start();
//		}
//		Thread.sleep(3000);
//		for (FutureTask<Integer> f : taskList) {
//			if (f.isDone()) {
//				System.out.printf("實現callable - 我是第%s次執行的！\n", f.get());
//			}
//		}
//
//		//TreadPoolExecutor - CacheThreadPool
//	    new CacheThreadPoolSample().exec();
//
//	    //TreadPoolExecutor - FixThreadPool
//	    new FixedThreadPoolSample().exec();

//	    //TreadPoolExecutor - FixThreadPool WithCallable
//	    new FixedThreadPoolWithCallableSample().exec();
//
//	    //TreadPoolExecutor - SingleThreadExecutor
//	    new SingleThreadExecutorSample().exec();
//
//		//ScheduledThreadPoolExecutor - ScheduledThreadExecutor
//		new ScheduledThreadExecutorSample().exec();
//
//		//ScheduledThreadPoolExecutor - SingleScheduledThreadExecutor
//		new SingleScheduledThreadExecutorSample().exec();
//
		//Volatile
		//可見性
//		VolatileVisibleSample s = new VolatileVisibleSample();
//		s.exec();
		//非原子性
//		VolatileUnatomicSample vaSample = new VolatileUnatomicSample();
//		vaSample.exec();

//		//synchronized - unsafe
//		//sharedState++ 未被保護，導致多個線程同時取用導致計算錯誤
//		SynchronizedSample ss = new SynchronizedSample();
//		for(int i=0;i<2;i++) {
//			Thread t1 = new Thread(() -> {
//				ss.nonSafeAction();
//			});
//			t1.start();
//		}

//		//synchronized - safe
//		SynchronizedSample ss1 = new SynchronizedSample();
//		for(int i=0;i<2;i++) {
//			Thread t2 = new Thread(() -> {
//				ss1.safeAction();
//			});
//			t2.start();
//		}

//		//synchronized - safe by atomic class
//		SynchronizedSample ss2 = new SynchronizedSample();
//		for(int i=0;i<2;i++) {
//			Thread t3 = new Thread(() -> {
//				ss2.nonSafeActionButAtomic();
//			});
//			t3.start();
//		}

//		//CAS - safe
//		CasSample cs1 = new CasSample();
//		for(int i=0;i<2;i++) {
//			Thread t4 = new Thread(() -> {
//				cs1.implementByAtomic();
//			});
//			t4.start();
//		}

	}
}

class CasSample {
	private static AtomicBoolean flag = new AtomicBoolean(true);

	/**
	 * thread-0會將flag改為false，並等待5秒
	 * thread-1要修改值時會比對flag是否為true，不是的話就會retry
	 */
	public void implementByAtomic()
	{
		System.out.println("thread:"+Thread.currentThread().getName()+";flag:"+flag.get());
		if (flag.compareAndSet(true,false)){
			System.out.println(Thread.currentThread().getName()+" - "+flag.get());
			try{Thread.sleep(5000);}catch (InterruptedException e){}
			flag.set(true);//讓Thread-1通過
		}else{
			System.out.println("重試機制thread:"+Thread.currentThread().getName()+";flag:"+flag.get());
			try{Thread.sleep(500);}catch (InterruptedException e){}
			implementByAtomic();
		}
	}
}

class SynchronizedSample
{
	private int intSharedState = 0;
	private AtomicInteger atomicInteger = new AtomicInteger(0);
	public void nonSafeAction()
	{
		for(int i=0;i<123456789;i++)
		{
			intSharedState++;
		}
		System.out.println("未透過synchronized保證線程安全" + Thread.currentThread().getName() + " - intSharedState[" + intSharedState + "]");
	}

	public void nonSafeActionButAtomic()
	{
		for(int i=0;i<123456789;i++)
		{
			atomicInteger.getAndIncrement();
		}
		//等待另一個線程算完，否則一個先算完會得到不同的答案
		try{Thread.sleep(2000);}catch (InterruptedException e) {}
		System.out.println("透過原子操作類AtomicInteger保證線程安全" + Thread.currentThread().getName() + " - atomicInteger[" + atomicInteger + "]");
	}

	public void safeAction()
	{
		synchronized (this) //以this作為互斥單元
		{
			for(int i=0;i<123456789;i++)
			{
				intSharedState++;
			}
		}
		//等待另一個線程算完，否則一個先算完會得到不同的答案
		try{Thread.sleep(2000);}catch (InterruptedException e) {}
		System.out.println("透過synchronized保證線程安全" + Thread.currentThread().getName() + " - intSharedState[" + intSharedState + "]");
	}
}

class ExtendThreadSample extends Thread {
	@Override
	public void run() {
		System.out.println("繼承Thread - run Thread " + Thread.currentThread().getName() + " thread");
	}
}

class ImplementRunnableSample implements Runnable {
	public void run() {
		System.out.println("實現Runnable - run Runnable " + Thread.currentThread().getName() + " thread");
	}
}

class ImplementCallableSample implements Callable<Integer> {
	private volatile static int count = 0;

	public Integer call() throws Exception {
		System.out.println("實現callable - run Callable " + Thread.currentThread().getName() + " thread");
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
			System.out.println("----------complete----------");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			fixedThreadPool.shutdown();
		}
	}
}

class FixedThreadPoolWithCallableSample {
	public void exec() {
		List<Future<Integer>> resultList = new ArrayList<>();
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		try {
			for (int i = 0; i < 30; i++) {
				resultList.add(fixedThreadPool.submit(new ThreadPoolCallableMethod()));
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

/**
 *	volatile關鍵字的作用是：使變數在多個執行緒間可見（可見性）,強制執行緒從主記憶體中讀取。從而保證了多個執行緒的可見性	
 *	藉由設定volatile，讓此thread強制從main thread取isRunning變數，避免取不到而產生無窮迴圈
 */
class VolatileVisibleSample{
	public void exec() throws InterruptedException {
		VolatileVisibleImpl s = new VolatileVisibleImpl();
		s.start();
		Thread.sleep(1000);
		s.setRunning(false);
		System.out.println("Thread[" + Thread.currentThread().getName() + "]setRunning false complete");
	}
}

class VolatileVisibleImpl extends Thread {
	
	//private boolean isRunning = true;
	//設定了volatile，讓此thread強制從main thread取isRunning變數，避免取不到而產生無窮迴圈
    private volatile boolean isRunning = true;

    public void setRunning(boolean isRunning) {
		 System.out.println("Thread[" + Thread.currentThread().getName() + "] set isRunning[" + isRunning + "]");
		 this.isRunning = isRunning;
    }

    @Override
    public void run() {
        System.out.println("Thread[" + Thread.currentThread().getName() + "]開始無窮迴圈");
        while (isRunning == true) {
        }
        System.out.println("Thread[" + Thread.currentThread().getName() + "]執行緒執行完成了");
    }
}
class VolatileUnatomicSample {
	//100個Thread做count++ 100次，count應該要是1W，但結果卻不一定會到1W
	public void exec() throws InterruptedException {
		VolatileUnatomicImp[] mythreadArray = new VolatileUnatomicImp[100];
		for (int i = 0; i < 100; i++) {
		    mythreadArray[i] = new VolatileUnatomicImp();
		}
		for (int i = 0; i < 100; i++) {
		    mythreadArray[i].start();
		}
		//等執行續跑完
		Thread.sleep(3000);
		System.out.println("count = " + mythreadArray[0].getCount());
		System.out.println("count1 = " + mythreadArray[0].getCount1());
	}
}
class VolatileUnatomicImp extends Thread {
    public volatile static int count;
    public static int count1;

    private static void addCount() {
        for (int i = 0; i < 100; i++) {
            count++;
        }
    }
    
    private synchronized void addCount1() {
        for (int i = 0; i < 100; i++) {
        	setCount1(getCount1()+1);
        }
    }

    @Override
    public void run() {
        addCount();
        addCount1();
    }

	public static int getCount() {
		return count;
	}

	public int getCount1() {
		return count1;
	}

	public void setCount1(int count1) {
		VolatileUnatomicImp.count1 = count1;
	}
	
}

class ThreadPoolMethod implements Runnable {
	public void run() {
		String threadName = Thread.currentThread().getName();
		System.out.println("run " + threadName + " thread");
	}
}

class ThreadPoolCallableMethod implements Callable<Integer> {
	@Override
	public Integer call() throws Exception {
		try {
			String threadName = Thread.currentThread().getName();
//			Thread.sleep(1000);
			System.out.println("run " + threadName + " thread");
			return 1;
		}catch(Exception e) {
			return -1;
		}
	}
}