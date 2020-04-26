package org.digitalmind.buildingblocks.core.healthutils.dto;

import org.digitalmind.buildingblocks.core.healthutils.function.HealthIndicatorFunction;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.actuate.health.Health;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomHealthRequest implements HealthRequest {
    @Builder.Default
    private String operation = "Custom";
    private String name;
    private HealthIndicatorFunction healthIndicatorFunction;

    @Override
    public String getName() {
        return name + " - " + operation;
    }

    @Override
    public Health execute() {
        return healthIndicatorFunction.execute();
    }

}
