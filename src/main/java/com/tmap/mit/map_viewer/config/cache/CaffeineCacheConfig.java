package com.tmap.mit.map_viewer.config.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * caffein cache 적용
 * inmemory cache - simpleCache
 */

@Configuration
@RequiredArgsConstructor
public class CaffeineCacheConfig {
    /*private final ShpParserService shpParserService;

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
        return shpParserService.getShpParserDataNoCache(param);
    }*/
}
