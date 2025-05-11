package com.mahadisl.ratelimiter.service;

public interface RateLimiterService {

    Boolean tryConsume(String key);
}
