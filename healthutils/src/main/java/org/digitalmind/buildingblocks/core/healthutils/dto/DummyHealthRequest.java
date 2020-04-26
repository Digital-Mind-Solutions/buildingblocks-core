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
public class DummyHealthRequest implements HealthRequest {
    private static HealthRequestIndicatorFunction<DummyHealthRequest> healthRequestIndicatorFunction = HealthUtilService::dummy;

    private String name;
    private Map<String, Object> details;

    @Override
    public String getName() {
        return name + " - " + "Dummy";
    }

    @Override
    public Health execute() {
        return healthRequestIndicatorFunction.execute(this);
    }

}
