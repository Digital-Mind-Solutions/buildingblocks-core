package org.digitalmind.buildingblocks.core.jpautils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.digitalmind.buildingblocks.core.jpautils.converter.JpaJsonConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.JpaJsonObjectJsonConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.JpaMapJsonConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.JpaMapStringObjectJsonConverter;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaJsonSerializationPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.policy.JpaNoEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameter;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class Main {

    public static void main(String[] args) {
        JpaJsonConverter<Parameter> jpaJsonConverter =
                new JpaJsonConverter<Parameter>(
                        JpaNoEncryptionPolicy.INSTANCE,
                        new JpaJsonSerializationPolicy<Parameter>(Parameter.class, JpaJsonSerializationPolicy.OBJECT_MAPPER_TYPE)
                        );
        System.out.println(jpaJsonConverter.toString());

        TypeReference<LinkedHashMap<String, Parameter>> typeReference = new TypeReference<>() {};
        Type type = typeReference.getType();
        JpaMapJsonConverter<Parameter> jpaMapJsonConverter =
                new JpaMapJsonConverter<Parameter>(
                        JpaNoEncryptionPolicy.INSTANCE,
                        new JpaJsonSerializationPolicy<LinkedHashMap<String, Parameter>>(type, JpaJsonSerializationPolicy.OBJECT_MAPPER_TYPE)
                );
        System.out.println(jpaMapJsonConverter.toString());


        JpaJsonObjectJsonConverter jpaJsonObjectJsonConverter = new JpaJsonObjectJsonConverter();
        JpaMapStringObjectJsonConverter jpaMapStringObjectJsonConverter = new JpaMapStringObjectJsonConverter();

        System.out.println("done");

    }


}
