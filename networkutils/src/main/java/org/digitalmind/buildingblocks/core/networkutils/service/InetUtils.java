package org.digitalmind.buildingblocks.core.networkutils.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetUtils {

    public static String getHostName() {
        String hostname = null;

        try {
            String localHostName = InetAddress.getLocalHost().getHostName();
            if (!localHostName.equalsIgnoreCase("localhost")) {
                hostname = localHostName;
            }
        } catch (UnknownHostException e) {
        }

        // use docker container name

        if (hostname == null) {
            hostname = "UNDEFINED";
        }
        return hostname;
    }

}
