package com.willy.redis.cluster.rediscluster;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisclusterApplicationTests {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void hashTest() {
		System.out.println(redisTemplate.opsForHash().hasKey("key","hKey"));
		redisTemplate.opsForHash().put("key","hKey","value");
		System.out.println(redisTemplate.opsForHash().hasKey("key","hKey"));
		redisTemplate.opsForHash().delete("key","hKey");
		System.out.println(redisTemplate.opsForHash().hasKey("key","hKey"));
	}

}
