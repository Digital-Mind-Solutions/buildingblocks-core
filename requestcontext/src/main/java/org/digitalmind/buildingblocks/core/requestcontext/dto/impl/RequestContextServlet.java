package org.digitalmind.buildingblocks.core.requestcontext.dto.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Slf4j
public class RequestContextServlet extends AbstractRequestContext {

    private HttpServletRequest httpRequest;

    public RequestContextServlet(String id, Map<String, Object> details, Authentication authentication, Date date, Locale locale, HttpServletRequest httpRequest) {
        super(id, createDetails(details, httpRequest), authentication, date, createLocale(locale, httpRequest));
        this.httpRequest = httpRequest;
    }

    private static Locale createLocale(Locale locale, HttpServletRequest httpRequest) {
        if (locale != null) {
            return locale;
        }
        if (httpRequest == null) {
            return null;
        }
        if (httpRequest.getLocale() == null) {
            return null;
        }
        return httpRequest.getLocale();
    }

    private static String createClientXForwarded(HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return CLIENT_FORWARD_LIST_UNKNOWN;
        }
        String xFwd = httpRequest.getHeader("X-FORWARDED-FOR");
        if (xFwd == null) {
            xFwd = CLIENT_FORWARD_LIST_UNKNOWN;
        }
        return xFwd;
    }

    private static String createClientIpAddress(HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return CLIENT_IP_ADDRESS_UNKNOWN;
        }
        String ipAddress = httpRequest.getHeader("Client IP");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }
        if (ipAddress == null) {
            ipAddress = CLIENT_IP_ADDRESS_UNKNOWN;
        }
        return ipAddress;
    }

    private static Map<String, Object> createDetails(Map<String, Object> details, HttpServletRequest httpRequest) {
        Map<String, Object> map = new HashMap<>((details != null) ? details : new HashMap<>());
        map.put(CLIENT_IP_ADDRESS_ATTRIBUTE, createClientIpAddress(httpRequest));
        map.put(CLIENT_FORWARD_LIST_ATTRIBUTE, createClientXForwarded(httpRequest));
        return map;
    }

}
