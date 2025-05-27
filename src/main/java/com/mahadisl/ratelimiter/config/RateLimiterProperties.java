package com.mahadisl.ratelimiter.config;


import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Component
@ConfigurationProperties(prefix = "rate.limiter")
@Data
@Validated
public class RateLimiterProperties {

    // Defining default values
    @NonNull
    private int capacity = 10;

    @NonNull
    private int refillRate = 1;

    @NonNull
    private int refillTimeSeconds = 1;
}
