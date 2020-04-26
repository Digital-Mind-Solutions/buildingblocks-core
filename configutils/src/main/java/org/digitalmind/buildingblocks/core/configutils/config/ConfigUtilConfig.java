package org.digitalmind.buildingblocks.core.configutils.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.core.configutils.config.ConfigUtilModuleConfig.ENABLED;
import static org.digitalmind.buildingblocks.core.configutils.config.ConfigUtilModuleConfig.PREFIX;

@Configuration
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@ConfigurationProperties(prefix = PREFIX)
@EnableConfigurationProperties
@Getter
@Setter
public class ConfigUtilConfig {

    private boolean enabled;
    private String cacheSpecification;

}
