package org.digitalmind.buildingblocks.core.requestcontext.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.core.requestcontext.config.RequestContextModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.core.requestcontext.config.RequestContextModuleConfig.PREFIX;


@Configuration
@ConfigurationProperties(prefix = PREFIX)
@EnableConfigurationProperties
@Getter
@Setter
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
public class RequestContextConfig {
    private boolean enabled;
    private String cacheSpecification;
    private String defaultLocale;
}
