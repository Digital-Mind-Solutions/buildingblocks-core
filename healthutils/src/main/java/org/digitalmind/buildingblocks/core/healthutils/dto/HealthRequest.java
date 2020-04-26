package org.digitalmind.buildingblocks.core.healthutils.dto;

import org.springframework.boot.actuate.health.Health;

public interface HealthRequest {

    String getName();

    Health execute();

}
