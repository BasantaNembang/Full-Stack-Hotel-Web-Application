package com.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    public void saveOTP(String key, String otp){
        redisTemplate.opsForValue().set(key, otp, 60, TimeUnit.MINUTES);
    }

    public void saveUser(String key, String username){
        redisTemplate.opsForValue().set(key, username, 60, TimeUnit.MINUTES);
    }

    public String getOTPByKEY(String key){
       return redisTemplate.opsForValue().get(key);
    }

}
