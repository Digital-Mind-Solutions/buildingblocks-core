package org.digitalmind.buildingblocks.core.networkutils.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@Slf4j
public class HostUtilService {
    public static String SERVER_IP_ADDRESS_UNKNOWN = "unknown";
    public static String SERVER_HOST_NAME_UNKNOWN = "unknown";
    private static InetAddress inetAddress = inetAddress();

    private final InetUtils inetUtils;

    private final InetUtils.HostInfo hostInfo;

    private static final InetAddress inetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    @Autowired
    public HostUtilService(InetUtils inetUtils) {
        this.inetUtils = inetUtils;
        this.hostInfo = inetUtils.findFirstNonLoopbackHostInfo();
    }

    public String getIpAddress() {
        if (inetAddress != null) {
            return inetAddress.getHostAddress();
        }
        return SERVER_IP_ADDRESS_UNKNOWN;
    }

    public String getHostname() {
        if (inetAddress != null) {
            return inetAddress.getHostName();
        }
        return SERVER_HOST_NAME_UNKNOWN;
    }

}
