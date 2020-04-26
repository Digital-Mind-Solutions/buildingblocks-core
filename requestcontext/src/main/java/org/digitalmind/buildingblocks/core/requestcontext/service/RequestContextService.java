package org.digitalmind.buildingblocks.core.requestcontext.service;

import org.digitalmind.buildingblocks.core.requestcontext.dto.RequestContext;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface RequestContextService {

    RequestContext create(HttpServletRequest httpRequest);

    RequestContext create(HttpServletRequest httpRequest, Authentication authentication);

    RequestContext create(HttpServletRequest httpRequest, Authentication authentication, Map<String, Object> details);

    RequestContext create();

    RequestContext create(Map<String, Object> details);

    RequestContext create(String id);

}
