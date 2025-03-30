package org.digitalmind.buildingblocks.core.jpautils.converter;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

import java.util.LinkedHashMap;

@Slf4j
public abstract class JpaMapJsonConverter<T> extends JpaGenericConverter<LinkedHashMap<String, T>> {

    protected JpaMapJsonConverter(JpaAbstractEncryptionPolicy encryptionPolicy, JpaAbstractSerializationPolicy<LinkedHashMap<String, T>> serializationPolicy) {
        super(encryptionPolicy, serializationPolicy);
    }

}
