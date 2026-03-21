package org.digitalmind.buildingblocks.core.dtobase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
public abstract class AuditDTO extends BaseDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String createdBy;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date updatedAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String updatedBy;
}


