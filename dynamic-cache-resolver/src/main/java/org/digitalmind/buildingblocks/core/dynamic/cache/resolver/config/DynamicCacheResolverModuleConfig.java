package org.digitalmind.buildingblocks.core.dynamic.cache.resolver.config;

import org.digitalmind.buildingblocks.core.dynamic.cache.resolver.DynamicCacheResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = DynamicCacheResolverModuleConfig.ENABLED, havingValue = "true")
@Slf4j
public class DynamicCacheResolverModuleConfig extends CachingConfigurerSupport {
    public static final String PREFIX = "application.modules.common.dynamic-cache-resolver";
    public static final String ENABLED = PREFIX + ".enabled";
    public static final String CONFIG_PACKAGE = "org.digitalmind.buildingblocks.core.dynamic.cache.resolver.config";
    public static final String DYNAMIC_CACHE_RESOLVER = "dynamicCacheResolver";


    private final DynamicCacheResolverConfig config;
    private final ApplicationContext applicationContext;

    @Autowired
    public DynamicCacheResolverModuleConfig(DynamicCacheResolverConfig config, ApplicationContext applicationContext) {
        this.config = config;
        this.applicationContext = applicationContext;
    }

    @Bean(DYNAMIC_CACHE_RESOLVER)
    @ConditionalOnProperty(name = ENABLED, havingValue = "true")
    @Override
    public CacheResolver cacheResolver() {
        return new DynamicCacheResolver(config, applicationContext);
    }
}
