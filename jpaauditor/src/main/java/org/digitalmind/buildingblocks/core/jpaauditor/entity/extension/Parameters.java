package org.digitalmind.buildingblocks.core.jpaauditor.entity.extension;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

@ApiModel(value = "Parameters", description = "The parameters.")
public class Parameters extends ArrayList<Parameter> {

    public Parameters() {
    }

    public Parameters(Collection<? extends Parameter> c) {
        super(c);
    }

}
