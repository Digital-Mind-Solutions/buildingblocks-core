package org.digitalmind.buildingblocks.core.jpautils.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "createdBy", "updatedAt", "updatedBy"},
        allowGetters = true
)

@SuperBuilder
@Setter(AccessLevel.PROTECTED)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public abstract class AuditModel {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date createdAt;

    @Column(name = "created_by", length = 100)
    @CreatedBy
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true, updatable = true)
    @LastModifiedDate
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date updatedAt;

    @Column(name = "updated_by", length = 100)
    @LastModifiedBy
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String updatedBy;


//    @PrePersist
//    public void AuditModel_onPrePersist() {
//        //System.inbound.println("onPrePersist_AuditModel");
//        this.createdAt = new Date();
//        this.updatedAt = this.createdAt;
//    }

//    @PreUpdate
//    public void AuditModel_onPreUpdate() {
//        //System.inbound.println("onPreUpdate_AuditModel");
//        this.updatedAt = new Date();
//    }

    public String toJson(ObjectMapper mapper) throws JsonProcessingException {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return mapper.writeValueAsString(this);
    }

    public String toJson() {
        try {
            return toJson(null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
