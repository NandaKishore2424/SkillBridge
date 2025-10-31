package com.college.skillbridge.security;

import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

@Component
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 30;
    
    private LoadingCache<String, Integer> attemptsCache;
    
    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(BLOCK_DURATION_MINUTES, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) {
                    return 0;
                }
            });
    }
    
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }
    
    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (Exception e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }
    
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPTS;
        } catch (Exception e) {
            return false;
        }
    }
}