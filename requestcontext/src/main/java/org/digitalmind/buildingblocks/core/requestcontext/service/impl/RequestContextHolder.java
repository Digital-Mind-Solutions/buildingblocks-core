package org.digitalmind.buildingblocks.core.requestcontext.service.impl;

import org.digitalmind.buildingblocks.core.requestcontext.dto.RequestContext;

public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> requestContextHolder = new InheritableThreadLocal<>();

    public static void setContext(RequestContext requestContext) {
        requestContextHolder.set(requestContext);
    }

    public static RequestContext getContext() {
        return requestContextHolder.get();
    }

}
