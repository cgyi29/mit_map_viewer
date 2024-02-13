package com.tmap.mit.map_viewer.config.cache;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tmap.mit.map_viewer.cd.CaffeineCacheType;
import com.tmap.mit.map_viewer.cd.ManageLocale;
import com.tmap.mit.map_viewer.feature.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * caffein cache 적용
 * inmemory cache - simpleCache
 */

@Configuration
@RequiredArgsConstructor
public class CaffeineCacheConfig {
    private final FeatureService featureCollectionService;
    private final double x = 800;
    private final double y = 600;

    @Bean
    public CacheManager caffeineConfig() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        for(CaffeineCacheType cacheType : CaffeineCacheType.values()){
            for(ManageLocale locale : ManageLocale.values()) {
                LoadingCache<Object, Object> loadingCache = Caffeine.newBuilder()
                        .expireAfterWrite(cacheType.getRefreshAfterWrite(), cacheType.getTimeUtint())
                        .maximumSize(cacheType.getMaximumSize())
                        .build(key -> loadDataFromService(locale.name()));

                cacheManager.registerCustomCache(String.format(cacheType.getKeyFormat(), cacheType.getKey(), locale.name()), loadingCache);
            }
        }
        return cacheManager;
    }
    private Object loadDataFromService(String param) {
        return featureCollectionService.getFeatureCollectionByCountryNameWithCache(param, x, y);
    }
}
