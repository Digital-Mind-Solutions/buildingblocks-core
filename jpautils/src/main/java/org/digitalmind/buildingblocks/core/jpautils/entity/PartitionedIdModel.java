package org.digitalmind.buildingblocks.core.jpautils.entity;

public interface PartitionedIdModel<P, T> {
    P getPartitionKey();
    T getId();
}
