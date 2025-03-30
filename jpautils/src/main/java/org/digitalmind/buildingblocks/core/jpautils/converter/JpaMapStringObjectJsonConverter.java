package org.digitalmind.buildingblocks.core.jpautils.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaJsonSerializationPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaNoEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

import java.util.LinkedHashMap;

@Converter(autoApply = false)
public class JpaMapStringObjectJsonConverter extends JpaMapJsonConverter<Object> {
    private JpaMapStringObjectJsonConverter(
            JpaAbstractEncryptionPolicy encryptionPolicy,
            JpaAbstractSerializationPolicy<LinkedHashMap<String, Object>> serializationPolicy
    ) {
        super(encryptionPolicy, serializationPolicy);
    }

    public JpaMapStringObjectJsonConverter() {
        this(
                JpaNoEncryptionPolicy.INSTANCE,
                new JpaJsonSerializationPolicy<>(
                        new TypeReference<LinkedHashMap<String, Object>>() {
                        }.getType(),
                        JpaJsonSerializationPolicy.OBJECT_MAPPER_TYPE
                )
        );
    }

}
