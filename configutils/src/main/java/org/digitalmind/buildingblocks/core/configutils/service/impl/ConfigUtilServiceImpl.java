package org.digitalmind.buildingblocks.core.configutils.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.configutils.config.ConfigUtilConfig;
import org.digitalmind.buildingblocks.core.configutils.entity.Configuration;
import org.digitalmind.buildingblocks.core.configutils.exception.ConfigUtilException;
import org.digitalmind.buildingblocks.core.configutils.repository.ConfigurationRepository;
import org.digitalmind.buildingblocks.core.configutils.service.ConfigUtilService;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

import static org.digitalmind.buildingblocks.core.configutils.config.ConfigUtilModuleConfig.ENABLED;

@Service
@ConditionalOnProperty(name = ENABLED, havingValue = "true")
@Slf4j
@Transactional
public class ConfigUtilServiceImpl implements ConfigUtilService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigUtilConfig config;
    private final LoadingCache<ConfigurationKey, Configuration> cacheConfiguration;
    private final CacheLoader<ConfigurationKey, Configuration> loaderConfiguration;

    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ConfigurationKey {
        private String module;
        private String section;
    }

    @Autowired
    public ConfigUtilServiceImpl(
            ConfigurationRepository configurationRepository,
            ConfigUtilConfig config
    ) {
        this.configurationRepository = configurationRepository;
        this.config = config;
        this.loaderConfiguration = new CacheLoader<ConfigurationKey, Configuration>() {
            @Override
            public Configuration load(ConfigurationKey key) {
                return configurationRepository.findByModuleAndSection(key.getModule(), key.getSection());
            }
        };
        this.cacheConfiguration = CacheBuilder.from(config.getCacheSpecification()).build(this.loaderConfiguration);
    }

    @Override
    public Configuration getById(Long id) {
        return configurationRepository.getOne(id);
    }

    @Override
    public Configuration save(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    @Override
    public Configuration findByModuleAndSection(String module, String section) {
        return configurationRepository.findByModuleAndSection(module, section);
    }

    @Override
    public Parameter getParameter(String module, String section, String name) {
        Configuration configuration = null;
        try {
            configuration = this.cacheConfiguration.get(new ConfigurationKey(module, section));
        } catch (ExecutionException e) {
            throw new ConfigUtilException(e);
        } catch (CacheLoader.InvalidCacheLoadException e) {
            return null;
        }
        return configuration.getParameter(name);

    }

    @Override
    public <T> T getParameter(String module, String section, String name, Class<T> clazz) {
        Parameter parameter = getParameter(module, section, name);
        if (parameter != null) {
            return (T) parameter.getValue();
        }
        return null;
    }

    @Override
    public <T> T getParameter(String module, String section, String name, Class<T> clazz, T value) {
        T parameterValue = getParameter(module, section, name, clazz);
        if (parameterValue != null) {
            return parameterValue;
        }
        return value;
    }

}
