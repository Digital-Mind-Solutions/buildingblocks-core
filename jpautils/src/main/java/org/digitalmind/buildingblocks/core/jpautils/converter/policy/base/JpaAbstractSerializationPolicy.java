package org.digitalmind.buildingblocks.core.jpautils.converter.policy.base;

import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaSerializationConverterException;

public interface JpaAbstractSerializationPolicy<T> {
    String serialize(T data) throws JpaSerializationConverterException;

    T deserialize(String data) throws JpaSerializationConverterException;

}
