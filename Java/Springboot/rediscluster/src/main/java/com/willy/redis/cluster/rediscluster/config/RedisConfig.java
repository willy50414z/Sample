package com.willy.redis.cluster.rediscluster.config;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Component
public class RedisConfig {

  @Value("${spring.redis.cluster.nodes}")
  private String nodes;

  @Value("${spring.redis.password}")
  private String password;

  @Value("${spring.redis.jedis.pool.min-idle}")
  private int minIdle;

  @Value("${spring.redis.jedis.pool.max-active}")
  private int maxActive;

  @Value("${spring.redis.jedis.pool.max-idle}")
  private int maxIdle;

  @Bean
  public RedisTemplate<String, Object> getRedisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    //避免存入redis的時候key or value會被加上奇怪的符號
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    //設定連線
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }

  public JedisConnectionFactory jedisConnectionFactory() {
    //集群模式
    JedisConnectionFactory factory = new JedisConnectionFactory(getRedisClusterConfiguration(),
        getPoolConfig());
    factory.afterPropertiesSet();
    return factory;
  }

  //redis配置
  public JedisPoolConfig getPoolConfig() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMinIdle(minIdle);
    jedisPoolConfig.setMaxTotal(maxActive);
    jedisPoolConfig.setMaxIdle(maxIdle);
    return jedisPoolConfig;
  }

  public RedisClusterConfiguration getRedisClusterConfiguration() {
    RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
    String[] cNodes = nodes.split(",");
    Set<RedisNode> hp = new HashSet<>();
    for (String node : cNodes) {
      String[] split = node.split(":");
      hp.add(new RedisNode(split[0].trim(), Integer.parseInt(split[1])));
    }
    redisClusterConfiguration.setPassword(password);
    redisClusterConfiguration.setClusterNodes(hp);
    return redisClusterConfiguration;
  }
}

