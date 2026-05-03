package org.digitalmind.buildingblocks.core.jpautils.entity.generator;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(PartitionedIdCreateTableGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface PartitionedIdCreateTableId {

    String table();

    String pkColumnName() default "sequence_name";

    String valueColumnName() default "next_val";

    String pkColumnValue();

    int allocationSize() default 1;

    int initialValue() default 1;
}
