package com.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheInsPection {

        @Autowired
        private CacheManager cacheManager;

        public void printCache(String name){
           Cache cache =  cacheManager.getCache(name);
           if(cache!=null){
               System.out.println("Cache-Content");
               System.out.println(Objects.requireNonNull(cache.getNativeCache().toString()));
           }else{
               System.out.println("NO cache avaiable..");
           }

        }



}
