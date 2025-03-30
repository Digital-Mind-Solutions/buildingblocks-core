package org.digitalmind.buildingblocks.core.jpautils.converter;


import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

@Slf4j
public abstract class JpaJsonConverter<T> extends JpaGenericConverter<T> {
    protected JpaJsonConverter(JpaAbstractEncryptionPolicy encryptionPolicy, JpaAbstractSerializationPolicy<T> serializationPolicy) {
        super(
                encryptionPolicy,
                serializationPolicy
        );
    }

}
