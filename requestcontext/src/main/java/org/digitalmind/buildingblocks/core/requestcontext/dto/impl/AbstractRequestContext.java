package org.digitalmind.buildingblocks.core.requestcontext.dto.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.requestcontext.dto.RequestContext;
import org.springframework.security.core.Authentication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;


@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
@Slf4j
public abstract class AbstractRequestContext implements RequestContext {

    private static final SimpleDateFormat sdf;
    private static final InetAddress inetAddress;
    private static Locale defaultLocale;

    static {
        sdf = new SimpleDateFormat("yyMMdd");
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("Unable to initialize InetAddress", e);
        }
        inetAddress = ia;
        defaultLocale = new Locale("ro", "RO");
    }

    private final String id;
    private final Map<String, Object> details;
    private final Authentication authentication;
    private final Date date;
    private final Locale locale;

    public AbstractRequestContext(String id, Map<String, Object> details, Authentication authentication, Date date, Locale locale) {
        this.date = (date != null) ? date : new Date();
        this.id = (id != null) ? id : createId(this.date);
        this.details = createDetails(details);
        this.authentication = authentication;
        this.locale = (locale != null) ? locale : defaultLocale;
    }

    @Synchronized
    public static void setDefaultLocale(Locale locale) {
        defaultLocale = locale;
    }

    private static String createId(Date date) {
        return sdf.format(new Date()) + UUID.randomUUID().toString().replace("-", "");
    }

    private static Map<String, Object> createDetails(Map<String, Object> details) {
        Map<String, Object> map = new HashMap<>((details != null) ? details : new HashMap<>());
        map.put(SERVER_IP_ADDRESS_ATTRIBUTE, createServerIpAddress());
        map.put(SERVER_HOST_NAME_ATTRIBUTE, createServerHostName());
        return map;
    }

    private static String createServerIpAddress() {
        if (inetAddress != null) {
            return inetAddress.getHostAddress();
        }
        return SERVER_IP_ADDRESS_UNKNOWN;
    }

    private static String createServerHostName() {
        if (inetAddress != null) {
            return inetAddress.getHostName();
        }
        return SERVER_HOST_NAME_UNKNOWN;
    }

    public String getServerIpAddress() {
        return createServerIpAddress();
    }

    public String getServerHostName() {
        return createServerHostName();
    }

    public Locale getLocale() {
        return (this.locale != null) ? this.locale : defaultLocale;
    }

}
