package me.fly.spring.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuyafei on 16/6/14.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.cache.redis.host}")
    private String REDIS_HOST;
    @Value(("${spirng.cahce.redis.port}"))
    private Integer REDIS_PORT;

    // user,session,preset,recording, srsGroup, securityPolicy
    @Value("#{'${spring.cache.name}'.split(',')}")
    private List<String> CACHE_NAME;
    @Value("#{'${spring.cache.ttl}'.split(',')}")
    private List<Long> CACHE_TTL;
    @Value("#{'${spring.cache.maxsize}'.split(',')}")
    private List<Long> CACHE_MAXSIZE;

    @Bean
    @Primary
    public CacheManager guavaCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<GuavaCache> caches = new ArrayList<>();
        for(int i=0;i<CACHE_NAME.size();i++){
            System.out.println(CACHE_NAME.get(i)+CACHE_TTL.get(i)+CACHE_MAXSIZE.get(i));
            GuavaCache cache = new GuavaCache(CACHE_NAME.get(i), CacheBuilder.newBuilder()
                    .expireAfterWrite(CACHE_TTL.get(i), TimeUnit.SECONDS)
                    .maximumSize(CACHE_MAXSIZE.get(i))
                    .build());
            caches.add(cache);
        }

        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        // Defaults
        redisConnectionFactory.setHostName(REDIS_HOST);
        redisConnectionFactory.setPort(REDIS_PORT);
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(cf);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager redisCacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        /*
        List<String> caches = new ArrayList<String>();
        caches.add(CACHE_USER_NAME);
        caches.add(CACHE_SESSION_NAME);
        cacheManager.setCacheNames(caches);
*/
        Map<String, Long> expires = new HashMap<>();
        for(int i=0;i<CACHE_NAME.size();i++){
            expires.put(CACHE_NAME.get(i), CACHE_TTL.get(i));
        }
        cacheManager.setExpires(expires);
        // Number of seconds before expiration. Defaults to unlimited (0)
        // cacheManager.setDefaultExpiration(20);
        return cacheManager;
    }
}
