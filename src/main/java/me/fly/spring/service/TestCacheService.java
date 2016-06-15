package me.fly.spring.service;

import me.fly.spring.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * Created by wuyafei on 16/6/14.
 */
@Service
public class TestCacheService {

    @Cacheable("user")
    public User findUser(Long id,String firstName,String lastName){
        System.out.println("无缓存的时候调用这里");
        return new User(id,firstName,lastName);
    }

    @Cacheable(value = "session", cacheManager = "redisCacheManager", key = "#id")
    public User findUser2(Long id,String firstName,String lastName){
        System.out.println("无缓存的时候调用这里2");
        return new User(id,firstName,lastName);
    }

    @CacheEvict(value = "user")
    public void clearUserCache(Long id,String firstName,String lastName){
        System.out.println("user缓存已经清除");
    }

    @CacheEvict(value = "session", cacheManager = "redisCacheManager", key = "#id")
    public void clearSessionCache(Long id,String firstName,String lastName){
        System.out.println("session缓存已经清除");
    }
}
