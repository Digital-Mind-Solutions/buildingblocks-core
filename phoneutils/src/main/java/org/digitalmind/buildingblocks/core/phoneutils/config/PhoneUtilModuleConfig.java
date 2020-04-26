package org.digitalmind.buildingblocks.core.phoneutils.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.digitalmind.buildingblocks.core.phoneutils.config.PhoneUtilModuleConfig.SERVICE_PACKAGE;


@Configuration
@ComponentScan({
        SERVICE_PACKAGE
})
@Slf4j
public class PhoneUtilModuleConfig {
    public static final String MODULE = "phoneutils";
    public static final String PREFIX = "application.modules.common." + MODULE;
    public static final String ENABLED = PREFIX + ".enabled";
    public static final String API_ENABLED = PREFIX + ".api.enabled";

    public static final String ROOT_PACKAGE = "org.digitalmind.buildingblocks.core." + MODULE;
    public static final String CONFIG_PACKAGE = ROOT_PACKAGE + ".config";
    public static final String SERVICE_PACKAGE = ROOT_PACKAGE + ".service";
    public static final String API_PACKAGE = ROOT_PACKAGE + ".api";

}
