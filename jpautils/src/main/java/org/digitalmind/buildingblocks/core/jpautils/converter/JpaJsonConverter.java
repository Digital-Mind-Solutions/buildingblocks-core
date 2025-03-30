package org.digitalmind.buildingblocks.core.jpautils.converter;


import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

@Slf4j
@Converter
public class JpaJsonConverter<T> extends JpaGenericConverter<T> {
    public JpaJsonConverter(JpaAbstractEncryptionPolicy encryptionPolicy, JpaAbstractSerializationPolicy<T> serializationPolicy) {
        super(
                encryptionPolicy,
                serializationPolicy
        );
    }

}
