package com.mahadisl.ratelimiter.service;

public interface RateLimiterService {

    boolean tryConsume(String key);
}
