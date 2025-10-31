package com.college.skillbridge.aspect;

import com.college.skillbridge.annotation.CacheableWithTTL;
import com.college.skillbridge.annotation.SlowApiCache;
import com.college.skillbridge.annotation.UserSpecificCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.cache.Cache;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
public class CachingAspect {

    @Autowired
    private CacheManager cacheManager;

    @Around("@annotation(com.college.skillbridge.annotation.CacheableWithTTL)")
    public Object handleCacheableWithTTL(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheableWithTTL annotation = method.getAnnotation(CacheableWithTTL.class);

        String cacheKey = generateCacheKey(joinPoint, annotation.useKeyGenerator());
        Cache cache = cacheManager.getCache(annotation.cacheName());

        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
            if (valueWrapper != null) {
                return valueWrapper.get();
            }
        }

        Object result = joinPoint.proceed();
        if (cache != null && result != null) {
            cache.put(cacheKey, result);
        }

        return result;
    }

    @Around("@annotation(com.college.skillbridge.annotation.SlowApiCache)")
    public Object handleSlowApiCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SlowApiCache annotation = method.getAnnotation(SlowApiCache.class);

        String cacheKey = generateCacheKey(joinPoint, true);
        Cache cache = cacheManager.getCache(annotation.cacheName());

        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
            if (valueWrapper != null) {
                return valueWrapper.get();
            }
        }

        Object result = joinPoint.proceed();
        if (cache != null && result != null) {
            cache.put(cacheKey, result);
        }

        return result;
    }

    @Around("@annotation(com.college.skillbridge.annotation.UserSpecificCache)")
    public Object handleUserSpecificCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserSpecificCache annotation = method.getAnnotation(UserSpecificCache.class);

        String cacheKey = generateUserSpecificCacheKey(joinPoint, annotation.includeUserRole());
        Cache cache = cacheManager.getCache(annotation.cacheName());

        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
            if (valueWrapper != null) {
                return valueWrapper.get();
            }
        }

        Object result = joinPoint.proceed();
        if (cache != null && result != null) {
            cache.put(cacheKey, result);
        }

        return result;
    }

    private String generateCacheKey(ProceedingJoinPoint joinPoint, boolean useKeyGenerator) {
        if (!useKeyGenerator) {
            return joinPoint.getSignature().getName() + ":" + 
                   String.join(":", convertArgsToString(joinPoint.getArgs()));
        }
        return joinPoint.getTarget().getClass().getSimpleName() + ":" +
               joinPoint.getSignature().getName() + ":" +
               String.join(":", convertArgsToString(joinPoint.getArgs()));
    }

    private String generateUserSpecificCacheKey(ProceedingJoinPoint joinPoint, boolean includeRole) {
        StringBuilder key = new StringBuilder();
        key.append(joinPoint.getSignature().getName())
           .append(":");
        
        // Add method parameters
        key.append(String.join(":", convertArgsToString(joinPoint.getArgs())));

        // Add user role if specified
        if (includeRole) {
            // TODO: Implement actual user role retrieval logic
            key.append(":ROLE");
        }

        return key.toString();
    }

    private String[] convertArgsToString(Object[] args) {
        return Optional.ofNullable(args)
                .map(a -> java.util.Arrays.stream(a)
                        .map(arg -> arg != null ? arg.toString() : "null")
                        .toArray(String[]::new))
                .orElse(new String[0]);
    }
}