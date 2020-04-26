package org.digitalmind.buildingblocks.core.jpautils.entity;


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
        value = {"contextId", "createdAt", "createdBy", "updatedAt", "updatedBy"},
        allowGetters = true
)

@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public abstract class ContextAuditModel extends AuditModel {

    @Column(name = "context_id")
    //@NonNull
    private String contextId;

}
