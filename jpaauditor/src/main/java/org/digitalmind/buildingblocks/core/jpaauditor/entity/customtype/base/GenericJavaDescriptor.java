package org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.base;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.exception.TypeDescriptorException;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.CharacterStream;
import org.hibernate.engine.jdbc.WrappedClob;
import org.hibernate.engine.jdbc.internal.CharacterStreamImpl;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.DataHelper;
import org.hibernate.type.descriptor.java.MutabilityPlan;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

public class GenericJavaDescriptor<J> extends AbstractTypeDescriptor<J> {

    public static final ObjectMapper objectMapper = initializeObjectMapper();

    private static final ObjectMapper initializeObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    public GenericJavaDescriptor(Class<J> type, MutabilityPlan<J> mutabilityPlan) {
        super(type, mutabilityPlan);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    @Override
    public J fromString(String string) {
        try {
            return getObjectMapper().readValue(string, getJavaType());
        } catch (IOException ex) {
            throw new TypeDescriptorException("IO exception while transforming json text column in <" + this.getJavaType().getSimpleName() + "> object property", ex);
        }
    }

    @Override
    public String toString(J value) {
        try {
            return getObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new TypeDescriptorException("Error while transforming <" + this.getJavaType().getSimpleName() + "> to a text datatable column as json string", ex);
        }
    }

    @Override
    public <X> X unwrap(J value, Class<X> type, WrapperOptions options) {
        if (value == null)
            return null;

        if (value instanceof Clob) {
            if (CharacterStream.class.isAssignableFrom(type)) {
                try {
                    return (X) new CharacterStreamImpl(DataHelper.extractString(((Clob) value).getCharacterStream()));
                } catch (SQLException e) {
                    throw new HibernateException("Unable to access lob stream", e);
                }
            }
            final Clob clob = WrappedClob.class.isInstance(value)
                    ? ((WrappedClob) value).getWrappedClob()
                    : (Clob) value;
            return (X) clob;
        }

        if (String.class.isAssignableFrom(type))
            return (X) toString(value);

        throw unknownUnwrap(type);
    }

    @Override
    public <X> J wrap(X value, WrapperOptions options) {
        if (value == null)
            return null;

        if (Clob.class.isAssignableFrom(value.getClass())) {
            return (J) options.getLobCreator().wrap((Clob) value);
        }

        if (String.class.isInstance(value))
            return fromString((String) value);

        throw unknownWrap(value.getClass());
    }

}
