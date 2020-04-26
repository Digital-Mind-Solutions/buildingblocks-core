package org.digitalmind.buildingblocks.core.dynamic.cache.resolver.dto;

import org.digitalmind.buildingblocks.core.dynamic.cache.resolver.DynamicCacheResolver;
import lombok.*;
import org.springframework.cache.interceptor.BasicOperation;

import java.lang.reflect.Method;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class DynamicCacheDefinition {
    private String method;
    private String operation;
    @Singular
    private List<DynamicCacheProperties> caches;


    public static class DynamicCacheDefinitionBuilder {
        public DynamicCacheDefinitionBuilder method(Method method) {
            this.method = DynamicCacheResolver.calcMethodString(method);
            return this;
        }

        public DynamicCacheDefinitionBuilder method(String method) {
            this.method = method;
            return this;
        }

        public DynamicCacheDefinitionBuilder operation(BasicOperation operation) {
            this.operation = DynamicCacheResolver.calcOperationString(operation);
            return this;
        }

        public DynamicCacheDefinitionBuilder operation(String operation) {
            this.operation = operation;
            return this;
        }

    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode
    public static class DynamicCacheProperties {
        private String cacheManager;
        private String[] cacheNames;
    }

}
