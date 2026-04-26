package org.digitalmind.buildingblocks.core.jpautils.entity;

import java.util.function.BiFunction;

public interface PartitionedIdModel<P, T> extends IdModel<T> {

    String PARTITION_KEY_DELIMITER = "~";

    // =====================
    // CONTRACT
    // =====================
    P getPartitionKey();

    // =====================
    // PARSE
    // =====================
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

    // =====================
    // SERIALIZE
    // =====================
    static String calcIdentifier(Object partitionKey, Object id) {
        return String.valueOf(partitionKey)
                + PARTITION_KEY_DELIMITER
                + String.valueOf(id);
    }

    default String toIdentifier() {
        return calcIdentifier(this.getPartitionKey(), this.getId());
    }

    // =====================
    // VALIDARE (entry point)
    // =====================
    default boolean isValidIdentifier(String identifier) {
        return isValidIdentifier(identifier, this.getClass());
    }

    // =====================
    // VALIDARE CU MODEL (reflection)
    // =====================
    static boolean isValidIdentifier(String identifier, Class<?> modelClass) {
        Class<?> partitionKeyClass =
                IdModel.resolveTypeArgument(modelClass, PartitionedIdModel.class, 0);

        Class<?> idClass =
                IdModel.resolveTypeArgument(modelClass, PartitionedIdModel.class, 1);

        return isValidIdentifier(identifier, partitionKeyClass, idClass);
    }

    // =====================
    // VALIDARE DIRECTĂ (fără reflection)
    // =====================
    static boolean isValidIdentifier(
            String identifier,
            Class<?> partitionKeyClass,
            Class<?> idClass
    ) {
        if (identifier == null || identifier.isEmpty()) {
            return false;
        }

        int index = identifier.indexOf(PARTITION_KEY_DELIMITER);

        if (index <= 0 || index == identifier.length() - 1) {
            return false;
        }

        String partitionKey = identifier.substring(0, index);
        String id = identifier.substring(index + 1);

        return IdModel.isValidIdentifier(partitionKey, partitionKeyClass)
                && IdModel.isValidIdentifier(id, idClass);
    }

}