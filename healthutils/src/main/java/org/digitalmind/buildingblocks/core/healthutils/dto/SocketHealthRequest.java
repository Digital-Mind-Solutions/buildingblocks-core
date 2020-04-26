package org.digitalmind.buildingblocks.core.healthutils.dto;

import org.digitalmind.buildingblocks.core.healthutils.function.HealthRequestIndicatorFunction;
import org.digitalmind.buildingblocks.core.healthutils.service.HealthUtilService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.actuate.health.Health;

import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SocketHealthRequest implements HealthRequest {
    private static HealthRequestIndicatorFunction<SocketHealthRequest> healthRequestIndicatorFunction = HealthUtilService::socket;

    private String name;
    private String host;
    private int port;
    private long timeout;
    private Map<String, Object> details;

    @Override
    public String getName() {
        return name + " - " + "Socket";
    }

    @Override
    public Health execute() {
        return healthRequestIndicatorFunction.execute(this);
    }

}
