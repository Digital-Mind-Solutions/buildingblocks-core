package org.digitalmind.buildingblocks.core.jpautils.entity;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface PartitionedIdModel<P, T> extends Serializable {
    static final String PARTITION_KEY_DELIMITER = "~";

    P getPartitionKey();

    T getId();


    static <P, T, R extends PartitionedIdModel<P, T>> R fromString(
            String value,
            BiFunction<String, String, R> factory
    ) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        int index = value.indexOf(PARTITION_KEY_DELIMITER);

        if (index <= 0 || index == value.length() - 1) {
            throw new IllegalArgumentException("Invalid identifier: " + value);
        }

        String p = value.substring(0, index);
        String t = value.substring(index + 1);

        return factory.apply(p, t);
    }

    static String calcIdentifier(Object partitionKey, Object id) {
        String identifier =
                String.valueOf(partitionKey) +
                        PARTITION_KEY_DELIMITER +
                        String.valueOf(id);

        return identifier;
    }

    default String toIdentifier() {
        return calcIdentifier(this.getPartitionKey(), this.getId());
    }

}

