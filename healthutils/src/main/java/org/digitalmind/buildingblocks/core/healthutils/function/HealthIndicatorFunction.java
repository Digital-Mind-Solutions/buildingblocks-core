package org.digitalmind.buildingblocks.core.healthutils.function;

import org.springframework.boot.health.contributor.Health;

public interface HealthIndicatorFunction {
    Health execute();
}
