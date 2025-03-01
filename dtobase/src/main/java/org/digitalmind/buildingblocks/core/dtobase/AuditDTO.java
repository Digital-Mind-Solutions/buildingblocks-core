package org.digitalmind.buildingblocks.core.dtobase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@JsonPropertyOrder(
        {
                "createdAt", "createdBy", "updatedAt", "updatedBy"
        }
)
@Schema(value = "AuditDTO", description = "The base auditable object.")
public abstract class AuditDTO extends BaseDTO {

    @SchemaProperty(name = "Date when object was created", required = false, readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date createdAt;

    @SchemaProperty(name = "Person that created the object", required = false, readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String createdBy;

    @SchemaProperty(name = "Date when object was updated", required = false, readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date updatedAt;

    @SchemaProperty(name = "Person that updated the object", required = false, readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String updatedBy;
}


