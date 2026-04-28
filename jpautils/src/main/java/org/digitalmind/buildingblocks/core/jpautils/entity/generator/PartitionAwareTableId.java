package org.digitalmind.buildingblocks.core.jpautils.entity.generator;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(PartitionAwareTableGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
/**
 * Hibernate id generator based on {@code TableGenerator} that can also initialize the
 * partition key on entities implementing {@link PartitionAwareIdModel}.
 *
 * <p>Typical use-case:
 * the numeric id must be generated before INSERT (no IDENTITY),
 * then partition key can be derived from both entity state and generated id.</p>
 *
 * <p>Concrete example on an entity:</p>
 * <pre>
 * {@literal @}Entity
 * {@literal @}IdClass(PartitionedId.class)
 * public class ProcessDocument implements PartitionAwareIdModel&lt;Integer, Long&gt; {
 *
 *     {@literal @}Id
 *     {@literal @}PartitionAwareTableId(
 *             table = "id_generator",
 *             pkColumnName = "generator_name",
 *             valueColumnName = "generator_value",
 *             pkColumnValue = "process_document",
 *             allocationSize = 50,
 *             initialValue = 1
 *     )
 *     private Long id;
 *
 *     {@literal @}Id
 *     private Integer partitionKey;
 *
 *     {@literal @}Override
 *     public Integer calcPartitionKey(Long generatedId) {
 *         // Can use both "this" (entity fields) and generatedId
 *         return 202604;
 *     }
 * }
 * </pre>
 */
public @interface PartitionAwareTableId {

    String table();

    String pkColumnName() default "sequence_name";

    String valueColumnName() default "next_val";

    String pkColumnValue();

    int allocationSize() default 1;

    int initialValue() default 1;

}