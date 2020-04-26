package org.digitalmind.buildingblocks.core.healthutils.function;

import org.digitalmind.buildingblocks.core.healthutils.dto.HealthRequest;
import org.springframework.boot.actuate.health.Health;

public interface HealthRequestIndicatorFunction<T extends HealthRequest> {

    Health execute(T healthRequest);

}
