package com.hotel.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.HotelDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager manager(RedisConnectionFactory redisConnectionFactory){

        //cache for singel object
        RedisCacheConfiguration cacheConfiguration  =   RedisCacheConfiguration.defaultCacheConfig()
                                    .entryTtl(Duration.ofMinutes(10))
                                    .disableCachingNullValues()
                                    .serializeValuesWith(RedisSerializationContext.SerializationPair
                                     .fromSerializer(new Jackson2JsonRedisSerializer<>(HotelDto.class)));


        //cache for list of object
        ObjectMapper objectMapper = new ObjectMapper();
        //JavaType is a type to store  List<T> and so on in a serilzation and deserilization
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, HotelDto.class);
        Jackson2JsonRedisSerializer<List<HotelDto>> listTYpe = new Jackson2JsonRedisSerializer<>(type);

        RedisCacheConfiguration listConfiguration  =   RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(listTYpe));

        return  RedisCacheManager
               .builder(redisConnectionFactory)
               .withCacheConfiguration("rooms", listConfiguration)
               .cacheDefaults(cacheConfiguration)
               .build();
    }




}
