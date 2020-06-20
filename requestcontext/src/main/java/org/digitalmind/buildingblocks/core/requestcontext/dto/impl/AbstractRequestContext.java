package org.digitalmind.buildingblocks.core.requestcontext.dto.impl;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.digitalmind.buildingblocks.core.requestcontext.dto.RequestContext;
import org.springframework.security.core.Authentication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public abstract class AbstractRequestContext implements RequestContext {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    private static InetAddress inetAddress = inetAddress();
    protected static final Locale defaultLocale = new Locale("ro-RO");

    @Builder.Default
    private String id = createId();

    @Builder.Default
    private Map<String, Object> details = new HashMap<>();

    @Builder.Default
    private Authentication authentication = null;

    @Builder.Default
    private Date date = new Date();

    protected static final String createId() {
        return sdf.format(new Date()) + UUID.randomUUID().toString().replace("-", "");
    }

    private static final InetAddress inetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public String getServerIpAddress() {
        if (inetAddress != null) {
            return inetAddress.getHostAddress();
        }
        return SERVER_IP_ADDRESS_UNKNOWN;
    }

    public String getServerHostName() {
        if (inetAddress != null) {
            return inetAddress.getHostName();
        }
        return SERVER_HOST_NAME_UNKNOWN;
    }

    @Override
    public Map<String, Object> getDetails() {
        return new HashMap<String, Object>() {{
            putAll(details);
            put(SERVER_IP_ADDRESS_ATTRIBUTE, getServerIpAddress());
            put(SERVER_HOST_NAME_ATTRIBUTE, getServerHostName());
        }};
    }

}
