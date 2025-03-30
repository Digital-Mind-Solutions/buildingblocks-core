package org.digitalmind.buildingblocks.core.jpautils.converter;


import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaJsonSerializationPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaNoEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

@Slf4j
@Converter
public class JpaJsonConverter extends JpaGenericConverter<Object> {
    public JpaJsonConverter(JpaAbstractEncryptionPolicy encryptionPolicy, JpaAbstractSerializationPolicy serializationPolicy) {
        super(
                JpaNoEncryptionPolicy.INSTANCE,
                new JpaJsonSerializationPolicy<Object>()
        );
    }
}
