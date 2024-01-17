package com.tmap.mit.map_viewer.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tmap.mit.map_viewer.cd.CaffeineCacheType;
import com.tmap.mit.map_viewer.cd.TargetFile;
import com.tmap.mit.map_viewer.service.ShpParserServic;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * caffein cache 적용
 * inmemory cache - simpleCache
 */

@Configuration
@RequiredArgsConstructor
public class CaffeineCacheConfig {
    private final ShpParserServic shpParserServic;

    @Bean
    public CacheManager caffeineConfig() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        for(CaffeineCacheType cacheType : CaffeineCacheType.values()){
            for(TargetFile targetFile : TargetFile.values()) {
                LoadingCache<Object, Object> loadingCache = Caffeine.newBuilder()
                        .expireAfterWrite(cacheType.getRefreshAfterWrite(), cacheType.getTimeUtint())
                        .maximumSize(cacheType.getMaximumSize())
                        .build(key -> loadDataFromService(targetFile.name()));

                cacheManager.registerCustomCache(String.format(cacheType.getKeyFormat(), cacheType.getKey(), targetFile.name()), loadingCache);
            }
        }
        return cacheManager;
    }
    private Object loadDataFromService(String param) throws IOException {
        return shpParserServic.getShpParserDataNoCache(param);
    }
}
