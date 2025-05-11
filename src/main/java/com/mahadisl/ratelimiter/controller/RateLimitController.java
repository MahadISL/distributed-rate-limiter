package com.mahadisl.ratelimiter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rate-limit")
@RequiredArgsConstructor
public class RateLimitController {

    @GetMapping("/check")
    public ResponseEntity<?> checkLimit(@RequestParam String key) {
        System.out.println("Received check request for key: " + key);
        boolean allowed = true;

        if (allowed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }
}
