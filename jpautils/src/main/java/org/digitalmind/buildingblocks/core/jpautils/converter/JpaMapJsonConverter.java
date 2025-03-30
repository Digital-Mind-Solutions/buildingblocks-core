package org.digitalmind.buildingblocks.core.jpautils.converter;

import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

import java.util.LinkedHashMap;

@Slf4j
//@Converter
public class JpaMapJsonConverter<T> extends JpaGenericConverter<LinkedHashMap<String, T>> {

    public JpaMapJsonConverter(JpaAbstractEncryptionPolicy encryptionPolicy, JpaAbstractSerializationPolicy<LinkedHashMap<String, T>> serializationPolicy) {
        super(encryptionPolicy, serializationPolicy);
    }

}
