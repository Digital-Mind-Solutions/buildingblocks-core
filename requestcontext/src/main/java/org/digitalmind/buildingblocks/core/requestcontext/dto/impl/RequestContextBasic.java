package org.digitalmind.buildingblocks.core.requestcontext.dto.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Locale;

@SuperBuilder
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class RequestContextBasic extends AbstractRequestContext {

    private Locale locale;

    @Override
    public Locale getLocale() {
        return (locale != null) ? locale : defaultLocale;
    }
}

