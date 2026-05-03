package org.digitalmind.buildingblocks.core.jpautils.entity.generator;

import org.digitalmind.buildingblocks.core.jpautils.entity.PartitionedIdCreateModel;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Member;
import java.sql.*;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DB-backed generator for entities using an {@code @EmbeddedId}.
 * It reserves blocks from sequence table and consumes ids from memory.
 * <p>
 * All generator instances that use the same sequence name ({@link PartitionedIdCreateTableId#pkColumnValue()}) share
 * one {@link SegmentBlock} via {@link #SHARED_SEGMENT_BLOCKS} — fast path {@link AtomicLong} CAS, slow path
 * {@code synchronized} on that block plus DB I/O.
 */
public class PartitionedIdCreateTableGenerator implements IdentifierGenerator {
    private static final Logger log = LoggerFactory.getLogger(PartitionedIdCreateTableGenerator.class);

    /**
     * One in-memory block per sequence name ({@code pkColumnValue}) per JVM; multiple annotation sites may share it.
     */
    private static final ConcurrentHashMap<String, SegmentBlock> SHARED_SEGMENT_BLOCKS = new ConcurrentHashMap<>();

    private final String tableName;
    private final String pkColumnName;
    private final String valueColumnName;
    private final String pkColumnValue;
    private final int allocationSize;
    private final long initialValue;
    private final SegmentBlock segmentBlock;
    /**
     * Cached once per generator instance; product does not change for a datasource.
     */
    private volatile DbKind resolvedDbKind;

    @SuppressWarnings("unused")
    public PartitionedIdCreateTableGenerator(
            PartitionedIdCreateTableId config,
            Member member,
            GeneratorCreationContext context
    ) {
        this.tableName = config.table();
        this.pkColumnName = config.pkColumnName();
        this.valueColumnName = config.valueColumnName();
        this.pkColumnValue = config.pkColumnValue();
        this.allocationSize = Math.max(1, config.allocationSize());
        this.initialValue = Math.max(1L, config.initialValue());
        this.segmentBlock = SHARED_SEGMENT_BLOCKS.computeIfAbsent(config.pkColumnValue(), k -> new SegmentBlock());
    }

    @Override
    public Object generate(
            SharedSessionContractImplementor session,
            Object entity
    ) {
        Object generatedId = nextId(session);
        log.debug(
                "Generated id {} for entity {} using segment {}",
                generatedId,
                entity.getClass().getSimpleName(),
                pkColumnValue
        );
        Object embeddedId = ensureEmbeddedId(entity, generatedId);
        return embeddedId;
    }

    @Override
    public void registerExportables(Database database) {
        // No exportables in dummy implementation.
    }

    @Override
    public void initialize(SqlStringGenerationContext context) {
        // No initialization needed in dummy implementation.
    }

    private Long nextId(SharedSessionContractImplementor session) {
        return segmentBlock.nextId(this, session, pkColumnValue);
    }

    /**
     * Invoked only while holding {@code segment}'s monitor.
     */
    private void reserveBlockForSegment(
            SharedSessionContractImplementor session,
            String dbRowKey,
            SegmentBlock segment
    ) {
        final String selectForUpdate = "SELECT " + valueColumnName
                + " FROM " + tableName
                + " WHERE " + pkColumnName + " = ? FOR UPDATE";
        final String updateValue = "UPDATE " + tableName
                + " SET " + valueColumnName + " = ?"
                + " WHERE " + pkColumnName + " = ?";
        final String insertValue = "INSERT INTO " + tableName
                + " (" + pkColumnName + ", " + valueColumnName + ") VALUES (?, ?)";

        session.doWork(connection -> {
            try {
                ensureDbKindResolved(connection);
            } catch (SQLException metaEx) {
                throw new IllegalStateException("Cannot resolve database product for sequence reservation", metaEx);
            }
            DbKind dbKind = this.resolvedDbKind;
            for (int attempt = 0; attempt < 3; attempt++) {
                try {
                    Long currentNext = selectCurrentNextForUpdate(connection, selectForUpdate, dbRowKey);
                    if (currentNext == null) {
                        long start = initialValue;
                        long newNext = start + allocationSize;
                        tryInsertSegment(connection, insertValue, dbRowKey, newNext);
                        long inclusiveEnd = newNext - 1;
                        segment.nextIdToIssue.set(start);
                        segment.blockEndInclusive = inclusiveEnd;
                        log.debug(
                                "Initialized sequence segment {} in {} with range [{}..{}]",
                                dbRowKey,
                                tableName,
                                start,
                                inclusiveEnd
                        );
                        return;
                    }

                    long start = currentNext;
                    long newNext = start + allocationSize;
                    tryUpdateSegment(connection, updateValue, dbRowKey, newNext);
                    long inclusiveEnd = newNext - 1;
                    segment.nextIdToIssue.set(start);
                    segment.blockEndInclusive = inclusiveEnd;
                    log.debug(
                            "Reserved sequence block for segment {} in {}: [{}..{}]",
                            dbRowKey,
                            tableName,
                            start,
                            inclusiveEnd
                    );
                    return;
                } catch (SQLException ex) {
                    if (isDuplicateKey(ex, dbKind) && attempt < 2) {
                        log.debug(
                                "Duplicate key while reserving segment {} (attempt {}), retrying",
                                dbRowKey,
                                attempt + 1
                        );
                        continue;
                    }
                    log.error(
                            "Unable to reserve sequence block for segment {} in table {}",
                            dbRowKey,
                            tableName,
                            ex
                    );
                    throw new IllegalStateException(
                            "Unable to reserve sequence block for segment " + dbRowKey,
                            ex
                    );
                }
            }
            throw new IllegalStateException("Unable to reserve sequence block after retries");
        });
    }

    private void ensureDbKindResolved(Connection connection) throws SQLException {
        if (resolvedDbKind != null) {
            return;
        }
        synchronized (this) {
            if (resolvedDbKind != null) {
                return;
            }
            resolvedDbKind = resolveDbKind(connection);
        }
    }

    private static Long selectCurrentNextForUpdate(Connection connection, String sql, String dbRowKey)
            throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dbRowKey);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return null;
            }
        }
    }

    private static void tryInsertSegment(Connection connection, String sql, String dbRowKey, long nextVal)
            throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dbRowKey);
            ps.setLong(2, nextVal);
            ps.executeUpdate();
        }
    }

    private static void tryUpdateSegment(Connection connection, String sql, String dbRowKey, long nextVal)
            throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, nextVal);
            ps.setString(2, dbRowKey);
            int updated = ps.executeUpdate();
            if (updated != 1) {
                throw new SQLException("Segment row update affected " + updated + " rows");
            }
        }
    }

    /**
     * Detects unique / primary-key duplicate for retry only when {@link DbKind} matches:
     * vendor error codes differ across products (e.g. {@code 1} is Oracle ORA-00001 but meaningless on others).
     * H2 uses SQLSTATE {@code 23505} for unique violations (same state as PostgreSQL).
     */
    private static boolean isDuplicateKey(SQLException ex, DbKind dbKind) {
        SQLException current = ex;
        while (current != null) {
            if (isUniqueViolationFragment(current, dbKind)) {
                return true;
            }
            current = current.getNextException();
        }
        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof SQLException sqlCause && isUniqueViolationFragment(sqlCause, dbKind)) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private static boolean isUniqueViolationFragment(SQLException ex, DbKind dbKind) {
        String state = ex.getSQLState();
        int code = ex.getErrorCode();
        return switch (dbKind) {
            case POSTGRESQL, H2 -> "23505".equals(state);
            case MYSQL, MARIADB -> code == 1062;
            case ORACLE -> code == 1;
            case SQL_SERVER -> code == 2627 || code == 2601;
            case UNKNOWN -> false;
        };
    }

    private static DbKind resolveDbKind(Connection connection) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        String product = meta.getDatabaseProductName();
        if (product == null || product.isBlank()) {
            return DbKind.UNKNOWN;
        }
        String p = product.toLowerCase(Locale.ROOT);
        if (p.contains("postgres")) {
            return DbKind.POSTGRESQL;
        }
        if (p.contains("maria")) {
            return DbKind.MARIADB;
        }
        if (p.contains("mysql")) {
            return DbKind.MYSQL;
        }
        if (p.contains("oracle")) {
            return DbKind.ORACLE;
        }
        if (p.contains("microsoft sql server") || p.contains("sql server")) {
            return DbKind.SQL_SERVER;
        }
        if (p.contains("h2")) {
            return DbKind.H2;
        }
        return DbKind.UNKNOWN;
    }

    private enum DbKind {
        POSTGRESQL,
        MYSQL,
        MARIADB,
        ORACLE,
        SQL_SERVER,
        H2,
        UNKNOWN
    }

    /**
     * In-memory allocation window for one sequence table row. Fast path uses CAS on {@link #nextIdToIssue};
     * slow path uses {@code synchronized(this)} and {@link PartitionedIdCreateTableGenerator#reserveBlockForSegment}.
     */
    private static final class SegmentBlock {
        private final AtomicLong nextIdToIssue = new AtomicLong(1L);
        private volatile long blockEndInclusive;

        private Long nextId(
                PartitionedIdCreateTableGenerator owner,
                SharedSessionContractImplementor session,
                String dbRowKey
        ) {
            for (; ; ) {
                long end = blockEndInclusive;
                long candidate = nextIdToIssue.get();
                if (candidate > end) {
                    synchronized (this) {
                        if (nextIdToIssue.get() > blockEndInclusive) {
                            owner.reserveBlockForSegment(session, dbRowKey, this);
                        }
                    }
                    continue;
                }
                if (nextIdToIssue.compareAndSet(candidate, candidate + 1L)) {
                    return candidate;
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object ensureEmbeddedId(
            Object entity,
            Object generatedId
    ) {
        if (!(entity instanceof PartitionedIdCreateModel)) {
            log.error(
                    "Entity {} uses PartitionedIdCreateTableGenerator but does not implement PartitionedIdCreateModel",
                    entity.getClass().getName()
            );
            throw new IllegalStateException(
                    "Entity " + entity.getClass().getName()
                            + " uses PartitionedIdCreateTableGenerator but does not implement PartitionedIdCreateModel"
            );
        }
        PartitionedIdCreateModel embeddedIdAware = (PartitionedIdCreateModel) entity;

        Object embeddedId = embeddedIdAware.createKey(embeddedIdAware.getPartitionKey(), generatedId);
        if (embeddedId == null) {
            log.error("createKey(...) returned null for entity {}", entity.getClass().getName());
            throw new IllegalStateException(
                    "create(...) did not create a non-null id for entity " + entity.getClass().getName()
            );
        }
        return embeddedId;
    }

}
