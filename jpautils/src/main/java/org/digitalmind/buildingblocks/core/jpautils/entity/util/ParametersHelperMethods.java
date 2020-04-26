package org.digitalmind.buildingblocks.core.jpautils.entity.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameter;
import org.digitalmind.buildingblocks.core.jpautils.entity.extension.Parameters;

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

    default void clearParameter() {
        this.getParameters().clear();
    }

}
