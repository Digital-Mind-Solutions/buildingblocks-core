package org.digitalmind.buildingblocks.core.requestcontext.dto.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Slf4j
public class RequestContextBasic extends AbstractRequestContext {

    public RequestContextBasic(String id, Map<String, Object> details, Authentication authentication, Date date, Locale locale) {
        super(id, details, authentication, date, locale);
    }

}

