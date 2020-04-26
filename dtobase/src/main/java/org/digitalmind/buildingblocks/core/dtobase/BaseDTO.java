package org.digitalmind.buildingblocks.core.dtobase;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@ApiModel(value = "BaseDTO", description = "The base object.")
public abstract class BaseDTO {

    public abstract static class BaseDTOBuilder<C extends org.digitalmind.buildingblocks.core.dtobase.BaseDTO, B extends org.digitalmind.buildingblocks.core.dtobase.BaseDTO.BaseDTOBuilder<C, B>> {

    }
}
