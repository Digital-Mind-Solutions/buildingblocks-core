package org.digitalmind.buildingblocks.core.jpautils.converter;


import jakarta.persistence.Converter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaJsonSerializationPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaNoEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

@Converter(autoApply = false)
public class JpaJsonObjectJsonConverter extends JpaJsonConverter<Object> {
    private JpaJsonObjectJsonConverter(
            JpaAbstractEncryptionPolicy encryptionPolicy,
            JpaAbstractSerializationPolicy<Object> serializationPolicy
    ) {
        super(encryptionPolicy, serializationPolicy);
    }

    public JpaJsonObjectJsonConverter() {
        this(
                JpaNoEncryptionPolicy.INSTANCE,
                new JpaJsonSerializationPolicy<>(
                        Object.class,
                        JpaJsonSerializationPolicy.OBJECT_MAPPER_TYPE
                )
        );
    }

}
