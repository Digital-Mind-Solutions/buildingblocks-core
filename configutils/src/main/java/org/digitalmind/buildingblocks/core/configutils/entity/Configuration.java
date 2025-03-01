package org.digitalmind.buildingblocks.core.configutils.entity;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.digitalmind.buildingblocks.core.jpautils.entity.IdModel;
import org.digitalmind.buildingblocks.core.jpautils.entity.customtype.JpaParametersType;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;
import org.digitalmind.buildingblocks.core.jpautils.entity.util.ParametersHelperMethods;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;

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
@Schema(value = "Configuration", description = "Configuration entity.")
@JsonPropertyOrder(
        {
                "id",
                "module", "section",
                "parameters",
                "createdAt", "createdBy", "updatedAt", "updatedBy", "contextId"
        }
)
@TypeDefs({
        @TypeDef(name = "JpaParametersType", defaultForType = Parameters.class, typeClass = JpaParametersType.class)
})
public class Configuration extends ContextVersionableAuditModel implements IdModel<Long>, ParametersHelperMethods {

    public static final String TABLE_NAME = "configuration";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @SchemaProperty(name = "Unique id of the configuration", required = false)
    private Long id;

    @NotNull
    @Column(name = "module")
    @SchemaProperty(name = "Configuration module", required = false)
    private String module;

    @NotNull
    @Column(name = "section")
    @SchemaProperty(name = "Configuration module section", required = false)
    private String section;

    @NotNull
    @Column(name = "description", length = 1000)
    @SchemaProperty(name = "Configuration module description", required = false)
    private String description;


    @Column(name = "parameters", length = 4000)
    @Type(type = "JpaParametersType")
    private Parameters parameters;

}
