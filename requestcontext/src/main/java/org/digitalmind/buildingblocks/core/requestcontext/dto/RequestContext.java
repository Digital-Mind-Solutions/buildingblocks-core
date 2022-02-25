package org.digitalmind.buildingblocks.core.requestcontext.dto;

import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

public interface RequestContext {
    public static String SERVER_IP_ADDRESS_ATTRIBUTE = "Server Ip";
    public static String SERVER_HOST_NAME_ATTRIBUTE = "Server Name";
    public static String CLIENT_FORWARD_LIST_ATTRIBUTE = "Client X-Forwarded-For";
    public static String CLIENT_IP_ADDRESS_ATTRIBUTE = "Client Ip";
    public static String SERVER_IP_ADDRESS_UNKNOWN = "unknown";
    public static String SERVER_HOST_NAME_UNKNOWN = "unknown";
    public static String CLIENT_IP_ADDRESS_UNKNOWN = "unknown";
    public static String CLIENT_FORWARD_LIST_UNKNOWN = "unknown";

    Map<String, Object> getDetails();

    String getId();

    Authentication getAuthentication();

    Locale getLocale();

    Date getDate();

}
