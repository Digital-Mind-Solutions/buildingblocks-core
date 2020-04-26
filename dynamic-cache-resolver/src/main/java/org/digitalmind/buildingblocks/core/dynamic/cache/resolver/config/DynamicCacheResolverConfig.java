package org.digitalmind.buildingblocks.core.dynamic.cache.resolver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.core.dynamic.cache.resolver.config.DynamicCacheResolverModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.core.dynamic.cache.resolver.config.DynamicCacheResolverModuleConfig.PREFIX;

@Configuration
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@ConfigurationProperties(prefix = PREFIX)
@EnableConfigurationProperties
@Getter
@Setter
public class DynamicCacheResolverConfig {
    private boolean enabled;
}
