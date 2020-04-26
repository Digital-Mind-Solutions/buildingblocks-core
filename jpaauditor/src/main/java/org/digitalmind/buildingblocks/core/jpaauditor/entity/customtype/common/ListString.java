package org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.common;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class ListString extends LinkedList<String> {

    public ListString() {
    }

    public ListString(Collection<? extends String> c) {
        super(c);
    }

    public ListString(ListString listString) {
        this(listString != null ? listString.stream().collect(Collectors.toList()) : Collections.emptyList());
    }

}
