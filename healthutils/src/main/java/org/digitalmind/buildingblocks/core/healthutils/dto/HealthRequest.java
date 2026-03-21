package org.digitalmind.buildingblocks.core.healthutils.dto;

import org.springframework.boot.health.contributor.Health;

public interface HealthRequest {

    String getName();

    Health execute();

}
