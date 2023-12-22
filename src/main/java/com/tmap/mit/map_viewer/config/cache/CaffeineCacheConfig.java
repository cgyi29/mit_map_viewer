package com.tmap.mit.map_viewer.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.tmap.mit.map_viewer.cd.CaffeineCacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * caffein cache 적용
 * inmemory cache - simpleCache
 */

@Configuration
public class CaffeineCacheConfig {
    @Bean
    public CacheManager caffeineConfig() {
        List<CaffeineCache> caches = Arrays.stream(CaffeineCacheType.values())
                .map(cache -> new CaffeineCache(
                        cache.getCacheName(),
                        Caffeine.newBuilder()
                                .expireAfterWrite(cache.getExpiredAfterWrite(), cache.getTimeUtint())
                                .refreshAfterWrite(cache.getExpiredAfterWrite(), cache.getTimeUtint())
                                .maximumSize(cache.getMaximumSize())
                                .recordStats()
                                .build()
                )).toList();

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);

        return cacheManager;
    }
}
