package org.digitalmind.buildingblocks.core.jpautils.entity.generator;

import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.TableGenerator;

import java.lang.reflect.Member;
import java.util.Properties;

public class PartitionAwareTableGenerator implements IdentifierGenerator {

    private final TableGenerator delegate;

    public PartitionAwareTableGenerator(
            PartitionAwareTableId config,
            Member member,
            GeneratorCreationContext context
    ) {
        this.delegate = new TableGenerator();

        Properties props = new Properties();
        props.setProperty(TableGenerator.TABLE_PARAM, config.table());
        props.setProperty(TableGenerator.SEGMENT_COLUMN_PARAM, config.pkColumnName());
        props.setProperty(TableGenerator.VALUE_COLUMN_PARAM, config.valueColumnName());
        props.setProperty(TableGenerator.SEGMENT_VALUE_PARAM, config.pkColumnValue());
        props.setProperty(TableGenerator.INCREMENT_PARAM, String.valueOf(config.allocationSize()));
        props.setProperty(TableGenerator.INITIAL_PARAM, String.valueOf(config.initialValue()));

        this.delegate.configure(context, props);
    }

    @Override
    public Object generate(
            SharedSessionContractImplementor session,
            Object entity
    ) {
        Object generatedId = delegate.generate(session, entity);

        ensurePartitionKey(entity, generatedId);

        return generatedId;
    }

    @Override
    public void registerExportables(Database database) {
        delegate.registerExportables(database);
    }

    @Override
    public void initialize(SqlStringGenerationContext context) {
        delegate.initialize(context);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void ensurePartitionKey(
            Object entity,
            Object generatedId
    ) {
        if (!(entity instanceof PartitionAwareIdModel)) {
            throw new IllegalStateException(
                    "Entity " + entity.getClass().getName()
                            + " uses @PartitionAwareTableId but does not implement PartitionAwareIdModel"
            );
        }
        PartitionAwareIdModel partitionAware = (PartitionAwareIdModel) entity;

        if (partitionAware.getPartitionKey() != null) {
            return;
        }

        Object partitionKey = partitionAware.calcPartitionKey(generatedId);

        if (partitionKey == null) {
            throw new IllegalStateException(
                    "calcPartitionKey(...) returned null for entity " + entity.getClass().getName()
            );
        }

        partitionAware.setPartitionKey(partitionKey);
    }
}