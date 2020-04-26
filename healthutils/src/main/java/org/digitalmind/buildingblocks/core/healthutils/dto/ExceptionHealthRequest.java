package org.digitalmind.buildingblocks.core.healthutils.dto;


import org.digitalmind.buildingblocks.core.healthutils.service.HealthUtilService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.actuate.health.Health;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExceptionHealthRequest implements HealthRequest {
    private String name;
    private Throwable throwable;

    @Override
    public String getName() {
        return name + " - " + "Exception";
    }

    @Override
    public Health execute() {
        return HealthUtilService.exception(throwable);
    }

}
