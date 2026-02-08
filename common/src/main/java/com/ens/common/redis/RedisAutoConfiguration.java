package com.ens.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisAutoConfiguration {

    @Bean
    public RedisService redisService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        return new RedisService(redisTemplate, objectMapper);
    }
}
