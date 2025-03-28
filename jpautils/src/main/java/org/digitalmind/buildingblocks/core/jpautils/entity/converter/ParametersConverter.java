package org.digitalmind.buildingblocks.core.jpautils.entity.converter;


import jakarta.persistence.Converter;
import org.digitalmind.buildingblocks.core.jpautils.converter.base.JpaGenericConverter;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;

@Converter(autoApply = false)
public class ParametersConverter extends JpaGenericConverter<Parameters> {

    public ParametersConverter() {
        super(null, null);
    }

}
