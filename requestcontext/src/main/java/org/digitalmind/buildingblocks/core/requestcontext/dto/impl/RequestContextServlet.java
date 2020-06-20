package org.digitalmind.buildingblocks.core.requestcontext.dto.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class RequestContextServlet extends AbstractRequestContext {

    private HttpServletRequest httpRequest;
    private Locale locale;

    public RequestContextServlet(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
        initLocale();
    }

    public RequestContextServlet(String id, Map<String, Object> details, Authentication authentication, Date date, HttpServletRequest httpRequest) {
        super(id, details, authentication, date);
        this.httpRequest = httpRequest;
        initLocale();
    }

    public RequestContextServlet(AbstractRequestContextBuilder<?, ?> b, HttpServletRequest httpRequest) {
        super(b);
        this.httpRequest = httpRequest;
        initLocale();
    }

    private void initLocale() {
        try {
            this.locale = this.httpRequest.getLocale();
        } catch (Exception e) {
            this.locale = defaultLocale;
        }
    }

    public String getClientIpAddress() {
        if (httpRequest == null) {
            return null;
        }
        String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }
        return ipAddress;
    }

    @Override
    public Map<String, Object> getDetails() {
        Map details = super.getDetails();
        details.put("Client Ip Address", getClientIpAddress());
        return details;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

}

