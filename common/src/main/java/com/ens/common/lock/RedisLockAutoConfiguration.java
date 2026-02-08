package com.ens.common.lock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisLockAutoConfiguration {

    @Bean
    public RedisLockAspect redisLockAspect(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        return new RedisLockAspect(redisTemplate, objectMapper);
    }
}
