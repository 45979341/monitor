package com.zhuzhou.framework.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis 配置
 */
@Configuration
public class RedisConfigure {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.database}")
	private int database;

	@Value("${spring.redis.jedis.pool.max-active}")
	private int max_active;

	@Value("${spring.redis.jedis..pool.max-wait}")
	private int max_wait;

	@Value("${spring.redis.jedis..pool.max-idle}")
	private int max_idle;

	@Value("${spring.redis.jedis..pool.min-idle}")
	private int min_idle;

	@Value("${spring.redis.timeout}")
	private int time_out;

	/**
	 * redis模板，存储关键字是字符串，值是Jdk序列化
	 *
	 * @param redisConnectionFactory
	 * @return
	 * @Description:
	 */
	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		// key序列化方式;但是如果方法上有Long等非String类型的话，会报类型转换错误；
		// Long类型不可以会出现异常信息;
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);
		redisTemplate.setStringSerializer(redisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * redis连接的基础设置
	 *
	 * @return
	 * @Description:
	 */
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration ();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setDatabase(database);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		JedisClientConfiguration.JedisClientConfigurationBuilder config = JedisClientConfiguration.builder();
		JedisClientConfiguration.JedisPoolingClientConfigurationBuilder builder = config.usePooling();
		builder.poolConfig(genericObjectPoolConfig());
		config.connectTimeout(Duration.ofMillis(time_out));
		JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
				config.build());
		return factory;
	}

	/**
	 * 连接池配置
	 *
	 * @return
	 * @Description:
	 */
	public GenericObjectPoolConfig genericObjectPoolConfig() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(max_idle);
		poolConfig.setMaxWaitMillis(max_wait);
		poolConfig.setMinIdle(min_idle);
		poolConfig.setMaxTotal(max_active);
		return poolConfig;
	}
}
