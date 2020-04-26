package org.digitalmind.buildingblocks.core.healthutils.service;

import org.digitalmind.buildingblocks.core.healthutils.dto.*;
import org.digitalmind.buildingblocks.core.healthutils.function.HealthIndicatorFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

import static org.springframework.boot.actuate.health.Status.DOWN;

@Service
@Slf4j
public class HealthUtilService {

    public CacheableHealth create(long cacheTimeMillis, HealthIndicatorFunction healthIndicatorFunction) {
        return CacheableHealth.builder()
                .cacheTimeMillis(cacheTimeMillis)
                .healthIndicatorFunction(healthIndicatorFunction)
                .build();
    }

    public static Health exception(Throwable e) {
        Health.Builder healthBuilder = Health.down();
        healthBuilder.withException(e);
        return healthBuilder.build();
    }

    public Health execute(HealthRequest... healthRequests) {
        Status status = Status.UP;
        Health.Builder healthBuilder = Health.unknown();
        boolean foundStatusUP = false;
        if (healthRequests != null) {
            for (HealthRequest healthRequest : healthRequests) {
                if (foundStatusUP) {
                    break;
                }
                Health health = healthRequest.execute();
                healthBuilder.withDetail(healthRequest.getName(), health);
                status = health.getStatus();
                foundStatusUP = foundStatusUP || Status.UP.equals(status);
            }
        }
        return healthBuilder.status(status).build();
    }

    private static String toIpAddress(InetAddress inet) {
        String ipAddr = "";
        byte[] addr = inet.getAddress();

        // Convert to dot representation
        for (int i = 0; i < addr.length; i++) {
            if (i > 0) {
                ipAddr += ".";
            }
            ipAddr += addr[i] & 0xFF;
        }
        return ipAddr;
    }

    public static Health dummy(DummyHealthRequest dummyHealthRequest) {
        Health.Builder healthBuilder = Health.up();
        if (dummyHealthRequest.getDetails() != null) {
            dummyHealthRequest.getDetails().entrySet().stream().forEach(entry -> healthBuilder.withDetail(entry.getKey(), entry.getValue()));
        }
        healthBuilder.withDetail("operation", "dummy");
        return healthBuilder.build();
    }

    public static Health ping(PingHealthRequest pingHealthRequest) {
        Health.Builder healthBuilder = Health.up();
        if (pingHealthRequest.getDetails() != null) {
            pingHealthRequest.getDetails().entrySet().stream().forEach(entry -> healthBuilder.withDetail(entry.getKey(), entry.getValue()));
        }
        healthBuilder.withDetail("operation", "ping");
        try {
            InetAddress inet = InetAddress.getByName(pingHealthRequest.getHost());
            String ipAddr = toIpAddress(inet);
            healthBuilder.withDetail("destination", pingHealthRequest.getHost() + "/" + ipAddr);
            if (pingHealthRequest.getTimeout() > 0) {
                long pingStart = System.currentTimeMillis();
                boolean isReachable = inet.isReachable(Math.toIntExact(pingHealthRequest.getTimeout()));
                long pingEnd = System.currentTimeMillis();
                if (isReachable) {
                    healthBuilder.withDetail("status",
                            "Host is reachable - duration " + (pingEnd - pingStart) + " ms"
                    );
                } else {
                    healthBuilder.status(DOWN);
                    healthBuilder.withDetail(
                            "status",
                            "Host is NOT reachable - duration " + (pingEnd - pingStart) + " ms"
                    );
                }
            } else {
                healthBuilder.withDetail("status", "disabled");
            }
        } catch (IOException e) {
            healthBuilder.status(DOWN);
            healthBuilder.withException(e);
        }
        return healthBuilder.build();
    }

    public static Health socket(SocketHealthRequest socketHealthRequest) {
        Health.Builder healthBuilder = Health.up();
        if (socketHealthRequest.getDetails() != null) {
            socketHealthRequest.getDetails().entrySet().stream().forEach(entry -> healthBuilder.withDetail(entry.getKey(), entry.getValue()));
        }
        healthBuilder.withDetail("operation", "socket");
        try {
            InetAddress inet = InetAddress.getByName(socketHealthRequest.getHost());
            String ipAddr = toIpAddress(inet);
            healthBuilder.withDetail("destination", socketHealthRequest.getHost() + ":" + socketHealthRequest.getPort() + "/" + ipAddr + ":" + socketHealthRequest.getPort());
            if (socketHealthRequest.getTimeout() > 0) {
                long pingStart = System.currentTimeMillis();

                Socket pingSocket = null;
                PrintWriter out = null;
                BufferedReader in = null;
                try {
                    pingSocket = new Socket(ipAddr, socketHealthRequest.getPort());
                    out = new PrintWriter(pingSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
                    long pingEnd = System.currentTimeMillis();
                    healthBuilder.withDetail("status",
                            "Socket is accessible - duration " + (pingEnd - pingStart) + " ms");
                } catch (IOException e) {
                    healthBuilder.status(DOWN);
                    healthBuilder.withException(e);
                } finally {
                    try {
                        out.close();
                    } catch (Exception e) {
                        //do nothing
                    }
                    try {
                        in.close();
                    } catch (Exception e) {
                        //do nothing
                    }

                    try {
                        pingSocket.close();
                    } catch (Exception e) {
                        //do nothing
                    }
                }
            } else {
                healthBuilder.withDetail("status", "disabled");
            }
        } catch (IOException e) {
            healthBuilder.status(DOWN);
            healthBuilder.withException(e);
        }
        return healthBuilder.build();
    }


    public static Health web(WebHealthRequest webHealthRequest) {
        Health.Builder healthBuilder = Health.up();
        if (webHealthRequest.getDetails() != null) {
            webHealthRequest.getDetails().entrySet().stream().forEach(entry -> healthBuilder.withDetail(entry.getKey(), entry.getValue()));
        }

        healthBuilder.withDetail("operation", "web");
        healthBuilder.withDetail("destination", webHealthRequest.getUrl());
        try {
            URI uri = new URI(webHealthRequest.getUrl());
            HttpEntity requestEntity = new HttpEntity<>(null, null);
            ResponseEntity<String> result = webHealthRequest.getRestTemplate().exchange(uri, HttpMethod.GET, requestEntity, String.class);

            if (result.getStatusCode().value() != webHealthRequest.getStatus()) {
                healthBuilder.status(DOWN);
                healthBuilder.withDetail("error", "Health request returned http status " + result.getStatusCode().value() + ", expected value " + webHealthRequest.getStatus());
            }

            if (webHealthRequest.getMatcher() != null) {
                if (result.getBody() == null) {
                    healthBuilder.status(DOWN);
                    healthBuilder.withDetail("error", "Health request returned null body, expected to match " + webHealthRequest.getMatcher());
                } else {
                    if (!result.getBody().matches(webHealthRequest.getMatcher())) {
                        healthBuilder.status(DOWN);
                        healthBuilder.withDetail("error", "Health request returned body not matching " + webHealthRequest.getMatcher());
                        healthBuilder.withDetail("body", result.getBody());
                    }
                }
            }

        } catch (Exception e) {
            healthBuilder.status(DOWN);
            healthBuilder.withException(e);
        }
        return healthBuilder.build();
    }

}
