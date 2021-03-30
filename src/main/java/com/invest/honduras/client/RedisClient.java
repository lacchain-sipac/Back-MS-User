package com.invest.honduras.client;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.invest.honduras.config.ApplicationProperties;

@Component
public class RedisClient implements SessionClient {
    
	
    @Autowired
    private ApplicationProperties applicationProperties;

	private JedisConnectionFactory jedisFactory;
	private RedisTemplate<String, Object> redisTemplate;

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {
		jedisFactory = new JedisConnectionFactory();
		jedisFactory.setHostName(applicationProperties.getRedisHost());
		jedisFactory.setPort(applicationProperties.getRedisPort());
		jedisFactory.afterPropertiesSet();
		redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisFactory);
		redisTemplate.afterPropertiesSet();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

	}
	
	@Override
	public boolean closeSession(String key) {
		boolean close = false;
		Object jsonRedis = redisTemplate.opsForValue().get(key);
		if (jsonRedis != null) {
			redisTemplate.delete(key);
			close = true;
		}
		return close;
	}

	@Override
	public boolean existSession(String key) {
		Object jsonRedis = redisTemplate.opsForValue().get(key);
		if(jsonRedis != null) {
			return true ;
		}
		return false;
	}

}
