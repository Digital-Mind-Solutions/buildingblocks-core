package org.digitalmind.buildingblocks.core.requestcontext.dto.impl;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class RequestContextServlet extends AbstractRequestContext {

    private HttpServletRequest httpRequest;

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
        return (httpRequest.getLocale() != null) ? httpRequest.getLocale() : defaultLocale;
    }

}

