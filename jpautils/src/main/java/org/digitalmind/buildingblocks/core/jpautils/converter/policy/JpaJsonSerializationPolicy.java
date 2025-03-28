package org.digitalmind.buildingblocks.core.jpautils.converter.policy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaSerializationConverterException;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractSerializationPolicy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JpaJsonSerializationPolicy<T> implements JpaAbstractSerializationPolicy<T> {

    public final static ObjectMapper OBJECT_MAPPER_SIMPLE = initializeSimpleObjectMapper();

    public final static ObjectMapper OBJECT_MAPPER_TYPE = initializeTypeObjectMapper();

    protected final Map<MapperType, ObjectMapper> objectMapperMap;
    protected final Class<T> type;

    protected MapperType mapperType;

    public enum MapperType {
        SIMPLE,
        TYPE;

    }

    public JpaJsonSerializationPolicy(MapperType mapperType, ObjectMapper objectMapperSimple, ObjectMapper objectMapperType) {
        this.mapperType = mapperType;
        this.type = resolveTypeArgument();
        Map<MapperType, ObjectMapper> objectMapperMap = new ConcurrentHashMap<>();
        objectMapperMap.put(MapperType.SIMPLE, (objectMapperSimple == null ? OBJECT_MAPPER_SIMPLE : objectMapperSimple));
        objectMapperMap.put(MapperType.TYPE, (objectMapperType == null ? OBJECT_MAPPER_TYPE : objectMapperType));
        this.objectMapperMap = objectMapperMap;
    }

    public JpaJsonSerializationPolicy() {
        this(MapperType.SIMPLE, null, null);
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolveTypeArgument() {
        Type superType = getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType parameterizedType) {
            Type actual = parameterizedType.getActualTypeArguments()[0];
            if (actual instanceof Class<?> clazz) {
                return (Class<T>) clazz;
            }
        }

        /*
        --cod original
         Type actualTypeArgument = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        if (actualTypeArgument instanceof ParameterizedType) {
            this.type = (Class<T>) ((ParameterizedType) actualTypeArgument).getRawType();
        } else {
            this.type = (Class<T>) actualTypeArgument;
        }

         */
        throw new IllegalStateException("Unable to resolve generic type T for " + getClass().getName());
    }

    private static ObjectMapper initializeSimpleObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper = new ObjectMapper();
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

    private ObjectMapper getObjectMapper(MapperType mapperType) {
        return this.objectMapperMap.get(mapperType);
    }

    @Override
    public String serialize(T data) throws JpaSerializationConverterException {
        try {
            return getObjectMapper(this.mapperType).writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new JpaSerializationConverterException("Failed to serialize object to JSON", ex);
        }
    }

    @Override
    public T deserialize(String data) throws JpaSerializationConverterException {
        try {
            return getObjectMapper(this.mapperType).readValue(data, type);
        } catch (Exception ex) {
            throw new JpaSerializationConverterException("Failed to deserialize JSON to object", ex);
        }
    }

}
