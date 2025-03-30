package org.digitalmind.buildingblocks.core.jpautils.converter.base;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaConverterRuntimeException;
import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaEncryptionConverterException;
import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaSerializationConverterException;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;


@Slf4j
public abstract class JpaGenericConverter<C> implements AttributeConverter<C, String> {

    private JpaAbstractEncryptionPolicy encryptionPolicy;
    private JpaAbstractSerializationPolicy<C> serializationPolicy;
    private Class<C> type;

    public JpaGenericConverter(
            JpaAbstractEncryptionPolicy encryptionPolicy,
            JpaAbstractSerializationPolicy<C> serializationPolicy
    ) {
        this.encryptionPolicy = encryptionPolicy;
        this.serializationPolicy = serializationPolicy;
    }

    public void setEncryptionPolicy(JpaAbstractEncryptionPolicy encryptionPolicy) {
        this.encryptionPolicy = encryptionPolicy;
    }

    public void setSerializationPolicy(JpaAbstractSerializationPolicy<C> serializationPolicy) {
        this.serializationPolicy = serializationPolicy;
    }

    @Override
    public String convertToDatabaseColumn(C attribute) {
        if (attribute == null) return null;
        try {
            return this.encryptionPolicy.encrypt(this.serializationPolicy.serialize(attribute));
        } catch (JpaSerializationConverterException | JpaEncryptionConverterException ex) {
            throw new JpaConverterRuntimeException("Error while transforming <" + this.type.getSimpleName() + "> to a text datatable column as json string", ex);
        }
    }

    @Override
    public C convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            return this.serializationPolicy.deserialize(this.encryptionPolicy.decrypt(dbData));
        } catch (JpaSerializationConverterException | JpaEncryptionConverterException ex) {
            throw new JpaConverterRuntimeException("IO exception while transforming json text column in <" + this.type.getSimpleName() + "> object property", ex);
        }
    }

}
