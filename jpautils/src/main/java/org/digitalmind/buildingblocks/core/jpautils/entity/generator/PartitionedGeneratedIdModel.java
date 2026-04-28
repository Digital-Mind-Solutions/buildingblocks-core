package org.digitalmind.buildingblocks.core.jpautils.entity.generator;

public interface PartitionedGeneratedIdModel<P> {

    P getPartitionKey();

    void setPartitionKey(P partitionKey);

    P calcPartitionKey();
}