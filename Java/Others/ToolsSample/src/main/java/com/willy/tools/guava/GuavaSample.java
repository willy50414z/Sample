package com.willy.tools.guava;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class GuavaSample {
	public static void main(String[] args) throws ExecutionException {

		// 設定抓不到時預設回傳值
		CacheLoader<String, String> loader = new CacheLoader<String, String>() {
			public String load(String key) throws Exception {
				Thread.sleep(1000);
				if ("key".equals(key))
					return "123";
				System.out.println(key + " is loaded from a cacheLoader!");
				return key + "'s value";
			}
		};

		// cache清理監聽
		RemovalListener<String, String> removalListener = new RemovalListener<String, String>() {
			public void onRemoval(RemovalNotification<String, String> removal) {
				System.out.println("[" + removal.getKey() + ":" + removal.getValue() + "] is evicted!");
			}
		};

		// cache bean初始化
		LoadingCache<String, String> testCache = CacheBuilder.newBuilder()
				.maximumSize(7)
				.expireAfterWrite(10, TimeUnit.MINUTES)//寫入後10秒清掉cache
				.removalListener(removalListener)//加入cache清理監聽
				.build(loader);//加入cacheloader

		for (int i = 0; i < 10; i++) {
			String key = "key" + i;
			String value = "value" + i;
			testCache.put(key, value);
			System.out.println("[" + key + ":" + value + "] is put into cache!");
		}

		System.out.println(testCache.getIfPresent("key6"));

		System.out.println(testCache.get("key"));
		System.out.println(testCache.get("key99"));

	}
}
