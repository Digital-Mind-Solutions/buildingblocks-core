package org.digitalmind.buildingblocks.core.jpautils.entity;

import java.io.Serializable;

/**
 * Partition-scoped identity: {@link #getPartitionKey()} plus {@link IdModel#getId()}.
 */
public interface PartitionedIdModel<P, T> extends IdModel<T>, Serializable {

    P getPartitionKey();
}
