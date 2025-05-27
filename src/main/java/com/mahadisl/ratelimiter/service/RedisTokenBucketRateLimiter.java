package com.mahadisl.ratelimiter.service;

import com.mahadisl.ratelimiter.config.RateLimiterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class RedisTokenBucketRateLimiter implements RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private final RateLimiterProperties properties;
    private final DefaultRedisScript<Long> redisScript;

    RedisTokenBucketRateLimiter(StringRedisTemplate redisTemplate, RateLimiterProperties properties) {

        this.redisTemplate = redisTemplate;
        this.properties = properties;

        //Load lua script from file
        this.redisScript = new DefaultRedisScript<>();
        this.redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("scripts/rate_limiter.lua")
        ));
        this.redisScript.setResultType(Long.class);
    }

    @Override
    public boolean tryConsume(String key) {
        List<String> keys = Collections.singletonList(key);


        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        long requestedTokens = 1;

        try {
            Long result = redisTemplate.execute(
                    redisScript,
                    keys,
                    String.valueOf(properties.getCapacity()),
                    String.valueOf(properties.getRefillRate()),
                    String.valueOf(properties.getRefillTimeSeconds()),
                    String.valueOf(currentTimeSeconds),
                    String.valueOf(requestedTokens)
            );

            // Lua Script returns 1 if allowed 0 if denied
            return result != null && result == 1L;

        } catch (Exception e) {
            log.error("Error executing rate limiter script for key {}: {}", key, e.getMessage(), e);
            return false;
        }
    }
}
