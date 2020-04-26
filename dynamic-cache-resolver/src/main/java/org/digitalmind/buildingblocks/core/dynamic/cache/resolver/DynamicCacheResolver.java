package org.digitalmind.buildingblocks.core.dynamic.cache.resolver;

import org.digitalmind.buildingblocks.core.dynamic.cache.resolver.config.DynamicCacheResolverConfig;
import org.digitalmind.buildingblocks.core.dynamic.cache.resolver.dto.DynamicCacheDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.*;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.digitalmind.buildingblocks.core.dynamic.cache.resolver.config.DynamicCacheResolverModuleConfig.DYNAMIC_CACHE_RESOLVER;

@Slf4j
public class DynamicCacheResolver implements CacheResolver {
    private final DynamicCacheResolverConfig config;
    private final ApplicationContext applicationContext;
    private final Map<String, DynamicCacheDefinition> cacheDefinitionMap;
    private final Map<Method, String> methodGenericStringMap;
    private final Map<String, Collection<Cache>> resolvedCacheMap;

    @Autowired
    public DynamicCacheResolver(DynamicCacheResolverConfig config, ApplicationContext applicationContext) {
        this.config = config;
        this.applicationContext = applicationContext;
        this.cacheDefinitionMap = new ConcurrentHashMap<>();
        this.methodGenericStringMap = new ConcurrentHashMap<>();
        this.resolvedCacheMap = new ConcurrentHashMap<>();
    }



    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        if (context.getOperation() instanceof CacheOperation) {
            if (!DYNAMIC_CACHE_RESOLVER.equalsIgnoreCase(((CacheOperation) context.getOperation()).getCacheResolver())) {
                return Collections.emptyList();
            }
        }

        String key = this.getDynamicCacheDefinitionKey(context.getMethod(), context.getOperation());

        Collection<? extends Cache> resolveCaches = resolvedCacheMap.computeIfAbsent(key, t -> {
            Collection<Cache> result = new ArrayList();
            DynamicCacheDefinition dynamicCacheDefinition = this.cacheDefinitionMap.get(key);
            if (dynamicCacheDefinition != null) {
                Iterator<DynamicCacheDefinition.DynamicCacheProperties> caches = dynamicCacheDefinition.getCaches().iterator();
                while (caches.hasNext()) {
                    DynamicCacheDefinition.DynamicCacheProperties cacheProperties = caches.next();
                    if (!applicationContext.containsBean(cacheProperties.getCacheManager())) {
                        throw new IllegalArgumentException("Cannot find cache manager '" + cacheProperties.getCacheManager());
                    }
                    CacheManager cacheManager = (CacheManager) applicationContext.getBean(cacheProperties.getCacheManager());
                    for (String cacheName : cacheProperties.getCacheNames()) {
                        Cache cache = cacheManager.getCache(cacheName);
                        if (cache == null) {
                            throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for cache manager " + cacheProperties.getCacheManager());
                        }
                        result.add(cache);
                    }
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("DynamicCacheResolver :: Unable to find definition for the code " + key);
                }
            }
            if (log.isInfoEnabled()) {
                log.info("DynamicCacheResolver :: initialised definition for the code " + key + " (" + result.size() + " caches were identified)");
            }
            return result;
        });
        return resolveCaches;
    }

    public void registerCacheDefinition(DynamicCacheDefinition cacheDefinition) {
        this.cacheDefinitionMap.put(getDynamicCacheDefinitionKey(cacheDefinition), cacheDefinition);
    }

    public boolean containsCacheDefinition(DynamicCacheDefinition cacheDefinition) {
        return this.cacheDefinitionMap.containsKey(getDynamicCacheDefinitionKey(cacheDefinition));
    }

    public void unregisterCacheDefinition(DynamicCacheDefinition cacheDefinition) {
        if (containsCacheDefinition(cacheDefinition)) {
            this.cacheDefinitionMap.remove(getDynamicCacheDefinitionKey(cacheDefinition));
        }
    }

    protected String getDynamicCacheDefinitionKey(DynamicCacheDefinition cacheDefinition) {
        return getDynamicCacheDefinitionKey(cacheDefinition.getMethod(), cacheDefinition.getOperation());
    }

    protected String getDynamicCacheDefinitionKey(Method method, BasicOperation operation) {
        return getDynamicCacheDefinitionKey(getMethodString(method), getOperationString(operation));
    }

    protected String getDynamicCacheDefinitionKey(String method, String operation) {
        return method + "<*>" + operation;
    }

    protected String getMethodString(Method method) {
        return this.methodGenericStringMap.computeIfAbsent(method, m -> calcMethodString(m));
    }

    protected String getOperationString(BasicOperation operation) {
        return calcOperationString(operation);
    }


    public static String calcMethodString(Method method) {
        return method.toGenericString();
    }

    public static String calcOperationString(BasicOperation operation) {
        if (operation instanceof CachePutOperation) {
            return operation.getClass().getSimpleName() + "-" + ((CachePutOperation) operation).getKey();
        } else if (operation instanceof CacheableOperation) {
            return operation.getClass().getSimpleName() + "-" + ((CacheableOperation) operation).getKey();
        } else if (operation instanceof CacheEvictOperation) {
            return operation.getClass().getSimpleName() + "-" + ((CacheEvictOperation) operation).getKey();
        }
        return operation.getClass().getSimpleName();
    }
}
