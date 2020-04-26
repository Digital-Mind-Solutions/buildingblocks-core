package org.digitalmind.buildingblocks.core.requestcontext.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        RequestContextModuleConfig.SERVICE_PACKAGE
})
@ConditionalOnProperty(name = RequestContextModuleConfig.ENABLED, havingValue = "true")
public class RequestContextModuleConfig {

    public static final String MODULE = "requestcontext";
    public static final String PREFIX = "application.modules.common." + MODULE;
    public static final String ENABLED = PREFIX + ".enabled";
    public static final String API_ENABLED = PREFIX + ".api.enabled";

    public static final String ROOT_PACKAGE = "org.digitalmind.buildingblocks.core." + MODULE;
    public static final String CONFIG_PACKAGE = ROOT_PACKAGE + ".config";
    public static final String ENTITY_PACKAGE = ROOT_PACKAGE + ".entity";
    public static final String REPOSITORY_PACKAGE = ROOT_PACKAGE + ".repository";
    public static final String SERVICE_PACKAGE = ROOT_PACKAGE + ".service";
    public static final String API_PACKAGE = ROOT_PACKAGE + ".api";

    public static final String CACHE_NAME = MODULE + "-cache";
}
