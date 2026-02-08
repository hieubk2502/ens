package com.ens.common.lock;

import com.ens.common.annotation.RedisLock;
import com.ens.common.exception.AppException;
import com.ens.common.i18n.LabelKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.RequestBody;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RedisLockAspect {
    private static final String LOCK_PREFIX = "lock:";
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end",
            Long.class
    );

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisLockAspect(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(redisLock)")
    public Object lock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String lockKey = buildLockKey(joinPoint, redisLock);
        String token = UUID.randomUUID().toString();

        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, token, Duration.ofSeconds(redisLock.ttlSeconds()));
        if (Boolean.FALSE.equals(acquired)) {
            throw new AppException(LabelKey.ERROR_LOCKED);
        }

        log.info("Lock acquired key={}", lockKey);

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    unlock(lockKey, token);
                }
            });
            return joinPoint.proceed();
        }

        try {
            return joinPoint.proceed();
        } finally {
            unlock(lockKey, token);
        }
    }

    private void unlock(String key, String token) {
        try {
            redisTemplate.execute(UNLOCK_SCRIPT, List.of(key), token);
            log.info("Lock released key={}", key);
        } catch (Exception ex) {
            log.warn("Lock release failed key={}", key, ex);
        }
    }

    private String buildLockKey(ProceedingJoinPoint joinPoint, RedisLock redisLock) {
        StringBuilder key = new StringBuilder(LOCK_PREFIX).append(redisLock.method());
        List<String> parts = new ArrayList<>();

        Object body = extractRequestBody(joinPoint);
        Map<String, Object> bodyMap = toMap(body);

        for (String field : redisLock.fields()) {
            Object value = bodyMap != null ? bodyMap.get(field) : null;
            parts.add(field + "=" + String.valueOf(value));
        }

        String principalValue = resolvePrincipal(redisLock.principalField());
        if (principalValue != null) {
            parts.add("principal=" + principalValue);
        }

        if (!parts.isEmpty()) {
            key.append(":").append(String.join(":", parts));
        }
        return key.toString();
    }

    private Object extractRequestBody(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                return args[i];
            }
        }

        if (args != null) {
            for (Object arg : args) {
                if (arg == null) {
                    continue;
                }
                if (isSimpleType(arg.getClass())) {
                    continue;
                }
                return arg;
            }
        }
        return null;
    }

    private Map<String, Object> toMap(Object body) {
        if (body == null) {
            return null;
        }
        if (body instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        try {
            return objectMapper.convertValue(body, Map.class);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private String resolvePrincipal(String principalField) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principalField == null || principalField.isBlank()) {
            return principal.toString();
        }
        try {
            if (principal instanceof Map<?, ?> map) {
                Object value = map.get(principalField);
                return value != null ? value.toString() : null;
            }
            String getter = "get" + Character.toUpperCase(principalField.charAt(0)) + principalField.substring(1);
            Object value = principal.getClass().getMethod(getter).invoke(principal);
            return value != null ? value.toString() : null;
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || Number.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class;
    }
}
