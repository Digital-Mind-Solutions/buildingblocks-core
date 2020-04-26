package org.digitalmind.buildingblocks.core.dtobase.expand;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.digitalmind.buildingblocks.core.dtobase.expand.MappingContext.Type.*;

@SuperBuilder
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true, includeFieldNames = false, doNotUseGetters = true)
@Data
@JsonPropertyOrder(
        {
                "include", "exclude"
        }
)
@ApiModel(value = "Expand", description = "The expand directive that is driving the DTO projections.")
public class MappingContext {

    private static String MATCH_ANY = "*";

    @ApiModelProperty(value = "list of expand path attributes to be included in the projection", required = false)
    @Singular
    private SortedSet<String> includes;

    @ApiModelProperty(value = "list of expand path attributes to be excluded from the projection", required = false)
    @Singular
    private SortedSet<String> excludes;

    public MappingContext(String[] includes, String[] excludes) {
        if (includes == null || includes.length == 0) {
            includes = new String[0];
        }
        if (excludes == null || excludes.length == 0) {
            excludes = new String[0];
        }
        this.includes = new TreeSet(Arrays.asList(includes).stream().filter(value -> !StringUtils.isEmpty(value)).map(value -> value.startsWith(".") ? value : "." + value).collect(Collectors.toList()));
        this.excludes = new TreeSet(Arrays.asList(excludes).stream().filter(value -> !StringUtils.isEmpty(value)).map(value -> value.startsWith(".") ? value : "." + value).collect(Collectors.toList()));
    }

    @Getter
    public enum Type {
        NO(0),
        GENERIC(1),
        NAMED(10);

        private final int value;

        Type(int value) {
            this.value = value;
        }

    }

    public boolean qualifies(String fieldName, int level) {
        Type includeFlag = NO;
        for (String include : includes) {
            int i = StringUtils.ordinalIndexOf(include, ".", level);
            if (i >= 0) {
                int j = StringUtils.ordinalIndexOf(include, ".", level + 1);
                String qualifier;
                if (j >= 0) {
                    qualifier = include.substring(i + 1, j);
                } else {
                    qualifier = include.substring(i + 1);
                }
                if (qualifier.equalsIgnoreCase(MATCH_ANY)) {
                    includeFlag = GENERIC;
                } else if (qualifier.equalsIgnoreCase(fieldName)) {
                    includeFlag = NAMED;
                    break;
                }
            } else {
                if (include.endsWith(MATCH_ANY)) {
                    includeFlag = GENERIC;
                }
            }
        }

        Type excludeFlag = NO;
        for (String exclude : excludes) {
            int i = StringUtils.ordinalIndexOf(exclude, ".", level);
            if (i >= 0) {
                int j = StringUtils.ordinalIndexOf(exclude, ".", level + 1);
                String qualifier;
                if (j >= 0) {
                    //qualifier = exclude.substring(i + 1, j);
                    qualifier = "";
                } else {
                    qualifier = exclude.substring(i + 1);
                }
                if (qualifier.equalsIgnoreCase(MATCH_ANY)) {
                    excludeFlag = GENERIC;
                } else if (qualifier.equalsIgnoreCase(fieldName)) {
                    excludeFlag = NAMED;
                    break;
                }
            } else {
                if (exclude.endsWith(MATCH_ANY)) {
                    excludeFlag = GENERIC;
                }
            }


        }

        return ((includeFlag.getValue() - excludeFlag.getValue()) > 0);

    }

}
