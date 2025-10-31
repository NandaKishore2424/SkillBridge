package com.college.skillbridge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableWithTTL {
    String cacheName();
    long timeToLiveSeconds() default 3600;
    boolean useKeyGenerator() default false;
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlowApiCache {
    String cacheName();
    long timeToLiveSeconds() default 7200;
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserSpecificCache {
    String cacheName();
    boolean includeUserRole() default false;
}