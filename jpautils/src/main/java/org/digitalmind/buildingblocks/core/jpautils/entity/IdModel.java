package org.digitalmind.buildingblocks.core.jpautils.entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

public interface IdModel<T> {

    T getId();

    static String calcIdentifier(Object id) {
        return String.valueOf(id);
    }

    default String toIdentifier() {
        return calcIdentifier(this.getId());
    }

    default boolean isValidIdentifier(String identifier) {
        Class<?> idClass = resolveIdClass(this.getClass());

        if (idClass == null) {
            return false;
        }

        return isValidIdentifier(identifier, idClass);
    }

    static boolean isValidIdentifier(String identifier, Class<?> idClass) {
        if (identifier == null || identifier.isEmpty()) {
            return false;
        }

        try {
            if (String.class.equals(idClass)) {
                return true;
            }

            if (Long.class.equals(idClass) || long.class.equals(idClass)) {
                Long.parseLong(identifier);
                return true;
            }

            if (Integer.class.equals(idClass) || int.class.equals(idClass)) {
                Integer.parseInt(identifier);
                return true;
            }

            if (UUID.class.equals(idClass)) {
                UUID.fromString(identifier);
                return true;
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    static Class<?> resolveIdClass(Class<?> clazz) {
        Class<?> current = clazz;

        while (current != null && current != Object.class) {

            for (Type type : current.getGenericInterfaces()) {
                Class<?> resolved = resolveIdClassFromType(type);
                if (resolved != null) {
                    return resolved;
                }
            }

            Type genericSuperclass = current.getGenericSuperclass();
            Class<?> resolved = resolveIdClassFromType(genericSuperclass);
            if (resolved != null) {
                return resolved;
            }

            current = current.getSuperclass();
        }

        return null;
    }

    static Class<?> resolveIdClassFromType(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();

            if (rawType instanceof Class<?> rawClass && IdModel.class.isAssignableFrom(rawClass)) {
                Type idType = parameterizedType.getActualTypeArguments()[0];

                if (idType instanceof Class<?> idClass) {
                    return idClass;
                }
            }
        }

        return null;
    }

}