package org.digitalmind.buildingblocks.core.healthutils.function;

import org.springframework.boot.actuate.health.Health;

public interface HealthIndicatorFunction {
    Health execute();
}
