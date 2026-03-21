package org.digitalmind.buildingblocks.core.healthutils.function;

import org.digitalmind.buildingblocks.core.healthutils.dto.HealthRequest;
import org.springframework.boot.health.contributor.Health;

public interface HealthRequestIndicatorFunction<T extends HealthRequest> {

    Health execute(T healthRequest);

}
