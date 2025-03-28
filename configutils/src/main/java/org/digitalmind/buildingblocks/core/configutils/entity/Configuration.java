package org.digitalmind.buildingblocks.core.configutils.entity;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.digitalmind.buildingblocks.core.jpautils.entity.IdModel;
import org.digitalmind.buildingblocks.core.jpautils.entity.converter.ParametersConverter;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;
import org.digitalmind.buildingblocks.core.jpautils.entity.util.ParametersHelperMethods;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = Configuration.TABLE_NAME,
        indexes = {
                @Index(name = Configuration.TABLE_NAME + "_ix01", columnList = "module, section", unique = true)
        }
)
@EntityListeners({AuditingEntityListener.class})

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(name = "Configuration", description = "Configuration entity.")
@JsonPropertyOrder(
        {
                "id",
                "module", "section",
                "parameters",
                "createdAt", "createdBy", "updatedAt", "updatedBy", "contextId"
        }
)

public class Configuration extends ContextVersionableAuditModel implements IdModel<Long>, ParametersHelperMethods {

    public static final String TABLE_NAME = "configuration";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Schema(name = "Unique id of the configuration")
    private Long id;

    @NotNull
    @Column(name = "module")
    @Schema(name = "Configuration module")
    private String module;

    @NotNull
    @Column(name = "section")
    @Schema(name = "Configuration module section")
    private String section;

    @NotNull
    @Column(name = "description", length = 1000)
    @Schema(name = "Configuration module description")
    private String description;


    @Column(name = "parameters", length = 4000, columnDefinition = "VARCHAR") // TEXT, VARCHAR or CLOB depending on DB
    @Convert(converter = ParametersConverter.class)
    private Parameters parameters;

}
