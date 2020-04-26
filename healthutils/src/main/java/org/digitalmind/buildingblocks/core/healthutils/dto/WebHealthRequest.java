package org.digitalmind.buildingblocks.core.healthutils.dto;

import org.digitalmind.buildingblocks.core.healthutils.service.HealthUtilService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WebHealthRequest implements HealthRequest {

    private String name;
    private RestTemplate restTemplate;
    private String url;
    @Builder.Default
    private int status = HttpStatus.OK.value();
    private String matcher;
    private Map<String, Object> details;

    @Override
    public String getName() {
        return name + " - " + "Web";
    }

    @Override
    public Health execute() {
        return HealthUtilService.web(this);
    }

}
