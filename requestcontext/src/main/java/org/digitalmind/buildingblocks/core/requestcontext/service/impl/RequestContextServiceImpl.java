package org.digitalmind.buildingblocks.core.requestcontext.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.digitalmind.buildingblocks.core.requestcontext.config.RequestContextConfig;
import org.digitalmind.buildingblocks.core.requestcontext.dto.RequestContext;
import org.digitalmind.buildingblocks.core.requestcontext.dto.impl.RequestContextBasic;
import org.digitalmind.buildingblocks.core.requestcontext.dto.impl.RequestContextServlet;
import org.digitalmind.buildingblocks.core.requestcontext.exception.RequestContextException;
import org.digitalmind.buildingblocks.core.requestcontext.service.RequestContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service("requestContextService")
@Slf4j
public class RequestContextServiceImpl implements RequestContextService {
    private final LoadingCache<String, RequestContext> contextCache;
    private final CacheLoader<String, RequestContext> contextCacheLoader;
    private final RequestContextConfig config;


    public RequestContextServiceImpl(
            RequestContextConfig config
    ) {
        this.config = config;
        this.contextCacheLoader = new CacheLoader<String, RequestContext>() {
            @Override
            public RequestContext load(String id) {
                RequestContext requestContext = createInternal(id, null);
                return requestContext;
            }
        };

        this.contextCache = CacheBuilder
                .from(CacheBuilderSpec.parse(config.getCacheSpecification()))
                .build(this.contextCacheLoader);
    }

    @Override
    public RequestContext create() {
        return create((Map<String, Object>) null);
    }

    @Override
    public RequestContext create(Map<String, Object> details) {
        return getAndCache(createInternal(null, details));
    }


    @Override
    public RequestContext create(HttpServletRequest httpRequest) {
        return create(httpRequest, SecurityContextHolder.getContext().getAuthentication(), null);
    }

    @Override
    public RequestContext create(HttpServletRequest httpRequest, Authentication authentication) {
        return create(httpRequest, authentication, null);
    }

    @Override
    public RequestContext create(HttpServletRequest httpRequest, Authentication authentication, Map<String, Object> details) {
        RequestContextServlet.RequestContextServletBuilder requestContextBuilder = RequestContextServlet.builder();

        requestContextBuilder
                .authentication(authentication);

        requestContextBuilder
                .httpRequest(httpRequest);

        if (details != null) {
            requestContextBuilder.details(details);
        }

        return getAndCache(requestContextBuilder.build());
    }


    @Override
    public RequestContext create(String id) {

        try {
            return contextCache.get(id);
        } catch (ExecutionException e) {
            throw new RequestContextException("ExecutionException", e);
        }

    }

    private RequestContext getAndCache(RequestContext requestContext) {
        contextCache.put(requestContext.getId(), requestContext);
        RequestContextHolder.setContext(requestContext);
        return requestContext;

    }

    private RequestContext createInternal(String id, Map<String, Object> details) {

        RequestContextBasic.RequestContextBasicBuilder requestContextBuilder =
                RequestContextBasic.builder();
        if (id != null) {
            requestContextBuilder
                    .id(id);
        }

        requestContextBuilder
                .authentication(SecurityContextHolder.getContext().getAuthentication());

        if (details != null) {
            requestContextBuilder.details(details);
        }

        RequestContext requestContext = requestContextBuilder.build();
        //RequestContextHolder.setContext(requestContext);
        return requestContext;
    }

}
