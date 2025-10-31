package com.college.skillbridge.config;

import com.college.skillbridge.annotation.CacheableWithTTL;
import com.college.skillbridge.annotation.SlowApiCache;
import com.college.skillbridge.annotation.UserSpecificCache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.interceptor.KeyGenerator;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000));
        
        // Register known cache names
        cacheManager.setCacheNames(Arrays.asList(
            "batches",
            "students",
            "trainers",
            "courses",
            "skills",
            "assessments"
        ));
        
        return cacheManager;
    }

    @Bean
    public KeyGenerator userSpecificKeyGenerator() {
        return (target, method, params) -> {
            UserSpecificCache annotation = method.getAnnotation(UserSpecificCache.class);
            StringBuilder key = new StringBuilder();
            
            // Add method name to the key
            key.append(method.getName());

            // Add parameters to the key
            for (Object param : params) {
                key.append(":");
                key.append(param != null ? param.toString() : "null");
            }

            // Include user role if specified
            if (annotation != null && annotation.includeUserRole()) {
                // Add logic to include user role in the cache key
                // This is a placeholder - implement actual user role retrieval
                key.append(":ROLE");
            }

            return key.toString();
        };
    }

    @Bean
    public KeyGenerator customCacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder key = new StringBuilder();
            
            // Add target class and method name
            key.append(target.getClass().getSimpleName())
               .append(":")
               .append(method.getName());

            // Add parameters
            for (Object param : params) {
                key.append(":")
                   .append(param != null ? param.toString() : "null");
            }

            return key.toString();
        };
    }
}