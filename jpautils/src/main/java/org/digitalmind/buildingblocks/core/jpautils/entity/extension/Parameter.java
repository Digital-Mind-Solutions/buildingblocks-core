package org.digitalmind.buildingblocks.core.jpautils.entity.extension;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.digitalmind.buildingblocks.core.jpautils.entity.enumeration.ParameterSource;
import org.digitalmind.buildingblocks.core.jpautils.entity.enumeration.ParameterType;

import java.util.Locale;

@Builder
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Schema(name = "Parameter", description = "Parameter definition.")
@JsonPropertyOrder(
        {
                "name",
                "type", "source",
                "format", "dataClass",
                "value"
        }
)
public class Parameter {

    @SchemaProperty(name = "The parameter name")
    private String name;

    @SchemaProperty(name = "The parameter type")
    @Builder.Default
    private ParameterType type = ParameterType.CONSTANT;

    @SchemaProperty(name = "The parameter source")
    @Builder.Default
    private ParameterSource source = ParameterSource.INTERN;

    @SchemaProperty(name = "The parameter order")
    @Builder.Default
    private int orderId = Integer.MAX_VALUE;

    @SchemaProperty(name = "The parameter contains a conversion format (if required)")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String format;

    @SchemaProperty(name = "The parameter icon")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String icon;

    @JsonProperty("class")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Class<?> dataClass;

    @SchemaProperty(name = "The parameter value")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object value;

    public Parameter() {
    }

    public Parameter(String name, Object value) {
        this(name, ParameterType.CONSTANT, ParameterSource.EXTERN, Integer.MAX_VALUE, null, null, null, value);
    }

    public Parameter(String name, ParameterType type, ParameterSource source, int orderId, String format, String icon, Class<?> dataClass, Object value) {
        this.name = name;
        this.type = type;
        this.source = source;
        this.orderId = orderId;
        this.format = format;
        this.icon = icon;
        this.dataClass = null;
        setValue(value);
    }

    public void setDataClass(Class<?> dataClass) {
        validate(dataClass, this.value);
        this.dataClass = dataClass;
    }

    public void setValue(Object value) {
        validate(this.dataClass, value);
        this.value = value;
        if (value != null && this.dataClass == null) {
            this.dataClass = value.getClass();
        }
    }

    @JsonIgnore
    public String getValueAsText() {
        return getValueAsText(Locale.getDefault());
    }

    @JsonIgnore
    public String getValueAsText(Locale locale) {
        return String.valueOf(this.getValue());
    }

    public static class ParameterTypeValueBuilder {
        private Class<?> dataClass;
        private Object value = null;

        public final ParameterTypeValueBuilder dataClass(Class<?> dataClass) {
            validate(dataClass, this.value);
            this.dataClass = dataClass;
            return this;
        }

        public final ParameterTypeValueBuilder value(Object value) {
            validate(this.dataClass, value);
            this.value = value;
            if (value != null && this.dataClass == null) {
                this.dataClass = value.getClass();
            }
            return this;
        }

    }

    private static void validate(Class<?> dataClass, Object value) {
        if (value != null && dataClass != null && !dataClass.equals(value.getClass())) {
            throw new IllegalArgumentException(Parameter.class.getSimpleName() + " The dataClass and value are inconsistent");
        }
    }
}
