package org.digitalmind.buildingblocks.core.healthutils.dto;

import org.digitalmind.buildingblocks.core.healthutils.function.HealthIndicatorFunction;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.actuate.health.Health;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CacheableHealth {

    @Builder.Default
    private long cacheTimeMillis = 60000;
    @Builder.Default
    private Health lastHealthInfo = null;
    @Builder.Default
    private long lastHealthTimeMillis = 0;
    @Builder.Default
    private HealthIndicatorFunction healthIndicatorFunction = null;

    public Health execute(boolean useCache) {
        long currentTimeMillis = System.currentTimeMillis();
        if (!useCache || useCache && (currentTimeMillis - this.lastHealthTimeMillis > cacheTimeMillis)) {
            synchronized (this) {
                if (!useCache || useCache && (currentTimeMillis - this.lastHealthTimeMillis > cacheTimeMillis)) {
                    this.lastHealthInfo = healthIndicatorFunction.execute();
                    this.lastHealthTimeMillis = currentTimeMillis;
                }
            }
        }
        return this.lastHealthInfo;
    }

}
