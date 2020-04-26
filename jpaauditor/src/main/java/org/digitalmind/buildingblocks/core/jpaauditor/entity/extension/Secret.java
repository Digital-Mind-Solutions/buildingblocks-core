package org.digitalmind.buildingblocks.core.jpaauditor.entity.extension;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.util.Date;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Secret", description = "Secret definition.")
@JsonPropertyOrder(
        {
                "prefix", "secret",
                "lastAccessDate",
                "validateCount", "remainingValidateCount",
                "createDate",
        }
)
public class Secret {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String prefix;

    private String value;

    private Date lastAccessDate;

    private int validateCount;

    private int remainingValidateCount;

    private Date createDate;

    public Secret(Secret secret) {
        this(secret.prefix, secret.value, secret.lastAccessDate, secret.validateCount, secret.remainingValidateCount, secret.createDate);
    }

    @JsonIgnore
    public String getKey() {
        return StringUtils.isEmpty(prefix) ? value : prefix + "-" + value;
    }

    public boolean validate(String value) {
        boolean validate = false;
        if (value != null) {
            if (this.remainingValidateCount > 0) {
                validate = value.equals(this.value);
            }
        }
        if (!validate && this.remainingValidateCount > 0) {
            this.remainingValidateCount = Math.max(this.remainingValidateCount - 1, 0);
        }
        this.lastAccessDate = new Date();
        return validate;
    }

    public void reset() {
        this.remainingValidateCount = this.validateCount;
    }

}
