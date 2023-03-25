package org.digitalmind.buildingblocks.core.jpautils.entity.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameter;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;

import java.util.Arrays;
import java.util.Collection;

public interface ParametersHelperMethods {

    Parameters getParameters();

    void setParameters(Parameters parameters);

    //------------------------------------------------------------------------------------------------------------------
    // PARAMETER HELPER METHODS
    //------------------------------------------------------------------------------------------------------------------

    @JsonIgnore
    default int getParameterCount() {
        if (this.getParameters() != null) {
            return this.getParameters().size();
        }
        return 0;
    }

    default boolean hasParameter(String name) {
        return this.getParameter(name) != null;
    }


    default Parameter getParameter(String name) {
        if (this.getParameters() == null) {
            return null;
        }
        return this.getParameters().stream().filter(parameter -> parameter.getName().equals(name)).findFirst().orElse(null);
    }

    default Parameter getParameterByName(String name) {
        return getParameter(name);
    }

    default Parameter setParameter(Parameter parameter) {
        if (getParameters() == null) {
            setParameters(new Parameters());
        }
        Parameter parameterFind = getParameter(parameter.getName());
        if (parameterFind != null) {
            this.getParameters().remove(parameterFind);
        }
        this.getParameters().add(parameter);
        return parameter;
    }

    default void clearParameters() {
        if (this.getParameters() != null) {
            this.getParameters().clear();
        }
    }

    default boolean hasParameterEqual(String name, Object value) {
        Parameter parameter = getParameterByName(name);
        if (parameter == null) {
            return false;
        }
        if (parameter.getValue() == null && value == null) {
            return true;
        }
        if (parameter.getValue() != null) {
            return parameter.getValue().equals(value);
        }
        return false;
    }

    default boolean hasParameterContainingExact(String name, Object value) {
        Parameter parameter = getParameterByName(name);
        if (parameter == null) {
            return false;
        }
        if (parameter.getValue() == null) {
            if (value == null) {
                return true;
            }
            return false;
        }
        if (parameter.getValue().equals(value)) {
            return true;
        }
        if (parameter.getValue().getClass().isArray()) {
            return Arrays.asList(parameter.getValue()).contains(value);
        }
        if (parameter.getValue() instanceof Collection) {
            return ((Collection) parameter.getValue()).contains(value);
        }
        return false;
    }

    default boolean hasParameterContainingLike(String name, String value) {
        if (value == null) {
            return false;
        }
        Parameter parameter = getParameterByName(name);
        if (parameter == null) {
            return false;
        }

        if (parameter.getValue() != null) {
            if (parameter.getValue().getClass().isArray()) {
                return Arrays.asList((Object[]) (parameter.getValue())).stream().filter(stringItem -> (String.valueOf(stringItem).indexOf(value) >= 0)).findAny().isPresent();
            }
            if (parameter.getValue() instanceof Collection) {
                return ((Collection) parameter.getValue()).stream().filter(stringItem -> (String.valueOf(stringItem).indexOf(value) >= 0)).findAny().isPresent();
            }
            if (parameter.getValue() instanceof String) {
                return ((String) parameter.getValue()).indexOf(value)>=0;
            }
        }
        return false;
    }

    default int getParameterValueCount(String name) {
        if (this.getParameters() == null) {
            return 0;
        }
        Parameter parameter = getParameterByName(name);
        if (parameter == null) {
            return 0;
        }
        if (parameter.getValue() != null) {
            if (parameter.getValue().getClass().isArray()) {
                return ((Object[]) (parameter.getValue())).length;
            }
            if (parameter.getValue() instanceof Collection) {
                return ((Collection) parameter.getValue()).size();
            }
        }
        return 1;
    }


}
