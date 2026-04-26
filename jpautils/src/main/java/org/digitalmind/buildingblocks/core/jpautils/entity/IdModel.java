package org.digitalmind.buildingblocks.core.jpautils.entity;

public interface IdModel<T> {
    T getId();


    static String calcIdentifier(Object id) {
        String identifier = String.valueOf(id);
        return identifier;
    }

    default String toIdentifier() {
        return calcIdentifier(this.getId());
    }

}
