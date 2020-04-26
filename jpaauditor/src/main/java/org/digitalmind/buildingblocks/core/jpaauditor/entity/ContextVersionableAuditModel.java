package org.digitalmind.buildingblocks.core.jpaauditor.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"contextId", "version", "createdAt", "createdBy", "updatedAt", "updatedBy"},
        allowGetters = true
)

@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public abstract class ContextVersionableAuditModel extends VersionableAuditModel {

    @Column(name = "context_id")
    //@NonNull
    private String contextId;

}
