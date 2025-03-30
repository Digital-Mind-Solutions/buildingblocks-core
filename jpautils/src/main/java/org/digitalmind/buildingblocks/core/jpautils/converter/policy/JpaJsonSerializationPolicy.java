package org.digitalmind.buildingblocks.core.jpautils.converter.policy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaSerializationConverterException;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

import java.lang.reflect.Type;

public class JpaJsonSerializationPolicy<T> implements JpaAbstractSerializationPolicy<T> {

    public static final ObjectMapper OBJECT_MAPPER_SIMPLE = initializeSimpleObjectMapper();
    public static final ObjectMapper OBJECT_MAPPER_TYPE = initializeTypeObjectMapper();

    protected final Type type;
    protected final ObjectMapper objectMapper;

    public JpaJsonSerializationPolicy(Type type, ObjectMapper objectMapper) {
        if (type == null) throw new IllegalArgumentException("Type must not be null");
        this.type = type;
        this.objectMapper = (objectMapper == null) ? OBJECT_MAPPER_SIMPLE : objectMapper;
    }

    public JpaJsonSerializationPolicy(Type type) {
        this(type, null);
    }

    private static ObjectMapper initializeSimpleObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        return objectMapper;
    }

    private static ObjectMapper initializeTypeObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    private ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    public String serialize(T data) throws JpaSerializationConverterException {
        try {
            return getObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new JpaSerializationConverterException("Failed to serialize object to JSON", ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(String data) throws JpaSerializationConverterException {
        try {
            JavaType javaType = getObjectMapper().getTypeFactory().constructType(type);
            return getObjectMapper().readValue(data, javaType);
        } catch (Exception ex) {
            throw new JpaSerializationConverterException("Failed to deserialize JSON to object", ex);
        }
    }
}
