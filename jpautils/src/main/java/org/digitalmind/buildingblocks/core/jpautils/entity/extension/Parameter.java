package org.digitalmind.buildingblocks.core.jpautils.entity.extension;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.digitalmind.buildingblocks.core.jpautils.entity.enumeration.ParameterSource;
import org.digitalmind.buildingblocks.core.jpautils.entity.enumeration.ParameterType;

import java.lang.reflect.Array;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Locale;

@Builder
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@ApiModel(value = "Parameter", description = "Parameter definition.")
@JsonPropertyOrder(
        {
                "name",
                "type", "source",
                "format", "dataClass",
                "value"
        }
)
public class Parameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "The parameter name", required = true)
    private String name;

    @ApiModelProperty(value = "The parameter type", required = true)
    @Builder.Default
    private ParameterType type = ParameterType.CONSTANT;

    @ApiModelProperty(value = "The parameter source", required = false)
    @Builder.Default
    private ParameterSource source = ParameterSource.INTERN;

    @ApiModelProperty(value = "The parameter order", required = false)
    @Builder.Default
    private int orderId = Integer.MAX_VALUE;

    @ApiModelProperty(value = "The parameter contains a conversion format (if required)", required = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String format;

    @ApiModelProperty(value = "The parameter icon", required = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String icon;

    @JsonProperty("class")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Class<?> dataClass;

    @ApiModelProperty(value = "The parameter value", required = false)
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
        validateSerializableValue(value);
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

    /**
     * Guardrail for ORM lifecycle compatibility: converted parameter values must be serializable.
     * Supports recursive checks for arrays/collections/maps.
     */
    private static void validateSerializableValue(Object value) {
        if (value == null) {
            return;
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                validateSerializableValue(Array.get(value, i));
            }
            return;
        }
        if (value instanceof Collection<?> collection) {
            for (Object item : collection) {
                validateSerializableValue(item);
            }
            return;
        }
        if (value instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                validateSerializableValue(entry.getKey());
                validateSerializableValue(entry.getValue());
            }
            return;
        }
        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException(Parameter.class.getSimpleName() + " value type must be serializable. Unsupported type: " + value.getClass().getName());
        }
    }
}
