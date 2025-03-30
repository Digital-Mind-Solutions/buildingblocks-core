package org.digitalmind.buildingblocks.core.jpautils.converter;

import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaJsonSerializationPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaNoEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

import java.util.LinkedHashMap;

@Slf4j
@Converter
public class JpaMapJsonConverter extends JpaGenericConverter<LinkedHashMap<String, Object>> {

    public JpaMapJsonConverter(JpaAbstractEncryptionPolicy encryptionPolicy, JpaAbstractSerializationPolicy<LinkedHashMap<String, Object>> serializationPolicy) {
        super(encryptionPolicy, serializationPolicy);
    }

    public JpaMapJsonConverter() {
        this(
                JpaNoEncryptionPolicy.INSTANCE,
                new JpaJsonSerializationPolicy<LinkedHashMap<String, Object>>()
        );
    }


}
