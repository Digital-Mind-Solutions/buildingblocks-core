package org.digitalmind.buildingblocks.core.jpautils.entity;

/**
 * Contract for entities using {@link PartitionedIdCreateTableGenerator}.
 *
 * <p>The generator allocates the numeric id, resolves the partition key, and then delegates
 * composite-id materialization to the entity via {@link #createKey(Object, Object)}.</p>
 *
 * <p>Implementations must build/set the final embedded/composite identifier state inside
 * {@code create(partitionKey, id)} so that the entity exposes a fully initialized identifier
 * before Hibernate continues with persistence.</p>
 *
 * @param <P> partition key type
 * @param <T> generated numeric id type
 * @param <K> composite id type
 */
public interface PartitionedIdCreateModel<P, T, K> extends PartitionedIdModel<P, T> {

    K createKey(P partitionKey, T id);
}