package org.digitalmind.buildingblocks.core.jpautils.entity.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.TableGenerator;

public class PartitionAwareTableGenerator extends TableGenerator {

    @Override
    public Object generate(
            SharedSessionContractImplementor session,
            Object entity
    ) {
        if (!(entity instanceof PartitionedGeneratedIdModel partitionedEntity)) {
            throw new UnsupportedOperationException(
                    "PartitionAwareTableGenerator can only be used with entities implementing " +
                            PartitionedGeneratedIdModel.class.getName() +
                            " but got: " + (entity != null ? entity.getClass().getName() : "null")
            );
        }
        if (partitionedEntity.getPartitionKey() == null) {
            Object partitionKey = partitionedEntity.calcPartitionKey();

            if (partitionKey == null) {
                throw new IllegalStateException(
                        "calcPartitionKey() returned null for " +
                                entity.getClass().getName()
                );
            }
            partitionedEntity.setPartitionKey(partitionKey);
        }
        return super.generate(session, entity);
    }

}