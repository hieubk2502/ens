package com.ens.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void setData(String key, Object value) {
        if (value == null) {
            removeData(key);
            return;
        }
        if (value instanceof String str) {
            redisTemplate.opsForValue().set(key, str);
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize value for key=" + key, ex);
        }
    }

    public void setData(String key, Object value, Duration ttl) {
        if (value == null) {
            removeData(key);
            return;
        }
        if (value instanceof String str) {
            redisTemplate.opsForValue().set(key, str, ttl);
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttl);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize value for key=" + key, ex);
        }
    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T getData(String key, Class<T> type) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        if (type == String.class) {
            return type.cast(value);
        }
        try {
            return objectMapper.readValue(value, type);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to deserialize value for key=" + key, ex);
        }
    }

    public void removeData(String key) {
        redisTemplate.delete(key);
    }
}
