package org.digitalmind.buildingblocks.core.jpaauditor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"version", "createdAt", "createdBy", "updatedAt", "updatedBy"},
        allowGetters = true
)

@SuperBuilder
@Setter(AccessLevel.PROTECTED)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public abstract class VersionableAuditModel extends AuditModel{

    @Setter(AccessLevel.PROTECTED)
    @Column(name = "version")
    @Version
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private long version;

}
