package org.digitalmind.buildingblocks.core.jpautils.entity.generator;

public interface PartitionAwareIdModel<P, T> {

    P getPartitionKey();

    void setPartitionKey(P partitionKey);

    P calcPartitionKey(T generatedId);

}
