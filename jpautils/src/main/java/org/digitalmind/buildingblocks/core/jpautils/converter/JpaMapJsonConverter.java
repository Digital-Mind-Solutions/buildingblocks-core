package org.digitalmind.eventorchestrator.converter;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.eventorchestrator.converter.base.JpaGenericConverter;
import org.digitalmind.eventorchestrator.converter.exception.JpaMapJsonConverterException;

import jakarta.persistence.Converter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Converter
public class JpaMapJsonConverter extends JpaGenericConverter<Map<String, Object>, LinkedHashMap<String, Object>> {

    public JpaMapJsonConverter() {
        super(JpaGenericConverter.MapperType.TYPE, false);
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        validateMapPayload(attribute);
        return super.convertToDatabaseColumn(attribute);
    }

    private static void validateMapPayload(Map<String, Object> attribute) {
        if (attribute == null) {
            return;
        }
        for (Map.Entry<?, ?> entry : attribute.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new JpaMapJsonConverterException(
                        "Map payload key must be String. Unsupported key type: " +
                                (entry.getKey() == null ? "null" : entry.getKey().getClass().getName())
                );
            }
            validateMapValue(entry.getValue());
        }
    }

    private static void validateMapValue(Object value) {
        if (value == null) {
            return;
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                validateMapValue(Array.get(value, i));
            }
            return;
        }
        if (value instanceof Collection<?> collection) {
            for (Object item : collection) {
                validateMapValue(item);
            }
            return;
        }
        if (value instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> nestedEntry : map.entrySet()) {
                if (!(nestedEntry.getKey() instanceof String)) {
                    throw new JpaMapJsonConverterException(
                            "Nested map payload key must be String. Unsupported key type: " +
                                    (nestedEntry.getKey() == null ? "null" : nestedEntry.getKey().getClass().getName())
                    );
                }
                validateMapValue(nestedEntry.getValue());
            }
            return;
        }
        // Keep backward compatibility with legacy TYPE-map behavior:
        // values do not need to implement java.io.Serializable as long as Jackson can serialize them.
    }

}

//public class JpaMapJsonConverter implements AttributeConverter<Map<String, Object>, String> {
//
//    private static final ObjectMapper objectMapper = getObjectMapper();
//
//    private static final ObjectMapper getObjectMapper() {
//        ObjectMapper om = new ObjectMapper();
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY);
//        om.enable(SerializationFeature.INDENT_OUTPUT);
//        return om;
//    }
//
//    @Override
//    public String convertToDatabaseColumn(Map<String, Object> attribute) {
//        try {
//            return objectMapper.writeValueAsString(attribute);
//        } catch (JsonProcessingException ex) {
//            throw new JpaMapJsonConverterException("Error while transforming Map to a text datatable column as json string", ex);
//        }
//    }
//
//    @Override
//    public Map<String, Object> convertToEntityAttribute(String dbData) {
//        try {
//            return objectMapper.readValue(dbData, HashMap.class);
//        } catch (IOException ex) {
//            throw new JpaMapJsonConverterException("IO exception while transforming json text column in Map property", ex);
//        }
//    }
//
//}
