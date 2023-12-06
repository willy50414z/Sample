package com.willy.redis.cluster.rediscluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RedisclusterApplication implements CommandLineRunner {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(RedisclusterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		redisTemplate.opsForValue().set("name", "555");
		System.out.println(redisTemplate.opsForHash().hasKey("key","hKey"));
		redisTemplate.opsForHash().put("key","hKey","value");
		System.out.println(redisTemplate.opsForHash().hasKey("key","hKey"));
		redisTemplate.opsForHash().delete("key","hKey");
		System.out.println(redisTemplate.opsForHash().hasKey("key","hKey"));
	}
}
