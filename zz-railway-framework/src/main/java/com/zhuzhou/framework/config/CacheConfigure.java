package com.zhuzhou.framework.config;

import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存配置
 */
@Configuration
@EnableCaching
@Slf4j
public class CacheConfigure extends CachingConfigurerSupport {

    @Autowired
    private JedisConnectionFactory factory;

    /**
     * 配置 RedisTemplate，设置序列化器
     * <pre>
     *     在类里面配置 RestTemplate，需要配置 key 和 value 的序列化类。
     *     key 序列化使用 StringRedisSerializer, 不配置的话，key 会出现乱码。
     * </pre>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // set key serializer
        StringRedisSerializer serializer = new StringRedisSerializer();
        // 设置key序列化类，否则key前面会多了一些乱码
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setStringSerializer(serializer);

        GenericJackson2JsonRedisSerializer fastSerializer = new GenericJackson2JsonRedisSerializer();
//        GenericFastJsonRedisSerializer fastSerializer = new GenericFastJsonRedisSerializer();
        template.setValueSerializer(fastSerializer);
        template.setHashValueSerializer(fastSerializer);
        // 如果 KeySerializer 或者 ValueSerializer 没有配置，则对应的 KeySerializer、ValueSerializer 才使用这个 Serializer
        template.setDefaultSerializer(fastSerializer);

        log.info("spring.redis.database: {}", factory.getDatabase());
        log.info("spring.redis.host: {}", factory.getHostName());
        log.info("spring.redis.port: {}", factory.getPort());
        log.info("spring.redis.timeout: {}", factory.getTimeout());
        log.info("spring.redis.password: {}", factory.getPassword());
        GenericObjectPoolConfig pool = factory.getPoolConfig();
        log.info("spring.redis.use-pool: {}", factory.getUsePool());
        log.info("spring.redis.min-idle: {}", pool.getMinIdle());
        log.info("spring.redis.max-idle: {}", pool.getMaxIdle());
        log.info("spring.redis.max-active: {}", pool.getMaxTotal());
        log.info("spring.redis.max-wait: {}", pool.getMaxWaitMillis());
        log.info("spring.redis.test-on-borrow: {}", pool.getTestOnBorrow());
        log.info("spring.redis.test-on-create: {}", pool.getTestOnCreate());
        log.info("spring.redis.test-while-idle: {}", pool.getTestWhileIdle());
        log.info("spring.redis.time-between-eviction-runs-millis: {}", pool
                .getTimeBetweenEvictionRunsMillis());

        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 如果 @Cacheable、@CachePut、@CacheEvict 等注解没有配置 key，则使用这个自定义 key 生成器
     * <pre>
     *     但自定义了缓存的 key 时，难以保证 key 的唯一性，此时最好指定方法名，比如：@Cacheable(value="", key="{#root.methodName, #id}")
     * </pre>
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (o, method, objects) -> {
            StringBuffer buffer = new StringBuffer(method.getName());
            if (objects.length > 0) {
                buffer.append(":");
            }
            for (Object arg : objects){
                if(arg == null){
                    continue;
                }
                if(ReflectionUtils.isComplexType(arg.getClass())){
                    buffer.append(ReflectionUtils.toString(arg));
                }else{
                    buffer.append(arg);
                }
            }
            return buffer.toString();
        };
    }
}