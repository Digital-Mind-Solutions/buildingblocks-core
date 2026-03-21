package org.digitalmind.buildingblocks.core.jpautils.converter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ParametersAttributeConverter implements AttributeConverter<Parameters, String> {

    private static final ObjectMapper MAPPER = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    @Override
    public String convertToDatabaseColumn(Parameters attribute) {
        if (attribute == null) return null;
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize Parameters", e);
        }
    }

    @Override
    public Parameters convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return MAPPER.readValue(dbData, Parameters.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize Parameters", e);
        }
    }
}

