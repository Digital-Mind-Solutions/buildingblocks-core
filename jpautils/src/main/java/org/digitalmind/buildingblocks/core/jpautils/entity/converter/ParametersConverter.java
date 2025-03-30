package org.digitalmind.buildingblocks.core.jpautils.entity.converter;


import jakarta.persistence.Converter;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaJsonSerializationPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaNoEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;

@Converter(autoApply = false)
public class ParametersConverter extends JpaGenericConverter<Parameters> {

    public ParametersConverter() {
        super(
                JpaNoEncryptionPolicy.INSTANCE,
                new JpaJsonSerializationPolicy<Parameters>(Parameters.class, JpaJsonSerializationPolicy.OBJECT_MAPPER_TYPE)
        );
    }

}
