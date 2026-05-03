package org.digitalmind.buildingblocks.core.jpautils.entity;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface IdModel<T> extends Serializable {

    T getId();

    // =====================
    // PARSERS REGISTRY
    // =====================
    Map<Class<?>, Function<String, ?>> IDENTIFIER_PARSERS = new ConcurrentHashMap<>(Map.ofEntries(
            Map.entry(String.class, s -> s),
            Map.entry(Long.class, Long::parseLong),
            Map.entry(long.class, Long::parseLong),
            Map.entry(Integer.class, Integer::parseInt),
            Map.entry(int.class, Integer::parseInt),
            Map.entry(Short.class, Short::parseShort),
            Map.entry(short.class, Short::parseShort),
            Map.entry(Byte.class, Byte::parseByte),
            Map.entry(byte.class, Byte::parseByte),
            Map.entry(BigInteger.class, BigInteger::new),
            Map.entry(BigDecimal.class, BigDecimal::new),
            Map.entry(UUID.class, UUID::fromString)
    ));

    // =====================
    // SERIALIZE
    // =====================
    static String calcIdentifier(Object id) {
        return String.valueOf(id);
    }

    default String toIdentifier() {
        return calcIdentifier(this.getId());
    }

    // =====================
    // PARSE (RENAMED CORECT)
    // =====================
    @SuppressWarnings("unchecked")
    static <T> T fromIdentifier(String identifier, Class<T> idClass) {
        if (identifier == null || identifier.isEmpty() || idClass == null) {
            return null;
        }

        Function<String, ?> parser = IDENTIFIER_PARSERS.get(idClass);

        if (parser == null) {
            throw new IllegalArgumentException("Unsupported identifier type: " + idClass.getName());
        }

        return (T) parser.apply(identifier);
    }

    // =====================
    // VALIDARE
    // =====================
    default boolean isValidIdentifier(String identifier) {
        Class<?> idClass = resolveTypeArgument(this.getClass(), IdModel.class, 0);
        return isValidIdentifier(identifier, idClass);
    }

    static boolean isValidIdentifier(String identifier, Class<?> idClass) {
        if (identifier == null || identifier.isEmpty() || idClass == null) {
            return false;
        }

        Function<String, ?> parser = IDENTIFIER_PARSERS.get(idClass);

        if (parser == null) {
            return false;
        }

        try {
            parser.apply(identifier);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    // =====================
    // GENERIC TYPE RESOLUTION
    // =====================
    static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> targetRawType, int argumentIndex) {
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            for (Type type : current.getGenericInterfaces()) {
                Class<?> resolved = resolveTypeArgumentFromType(type, targetRawType, argumentIndex);
                if (resolved != null) {
                    return resolved;
                }
            }

            Class<?> resolved = resolveTypeArgumentFromType(
                    current.getGenericSuperclass(),
                    targetRawType,
                    argumentIndex
            );

            if (resolved != null) {
                return resolved;
            }

            current = current.getSuperclass();
        }

        return null;
    }

    static Class<?> resolveTypeArgumentFromType(Type type, Class<?> targetRawType, int argumentIndex) {
        if (!(type instanceof ParameterizedType pt)) {
            return null;
        }

        Type rawType = pt.getRawType();

        if (!(rawType instanceof Class<?> rawClass)) {
            return null;
        }

        if (targetRawType.equals(rawClass)) {
            Type arg = pt.getActualTypeArguments()[argumentIndex];
            if (arg instanceof Class<?>) {
                return (Class<?>) arg;
            }
            return null;
        }

        for (Type iface : rawClass.getGenericInterfaces()) {
            Class<?> resolved = resolveTypeArgumentFromType(iface, targetRawType, argumentIndex);
            if (resolved != null) {
                return resolved;
            }
        }

        return resolveTypeArgumentFromType(rawClass.getGenericSuperclass(), targetRawType, argumentIndex);
    }

}