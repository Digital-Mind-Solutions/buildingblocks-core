package org.digitalmind.buildingblocks.core.jpaauditor.entity.util;

import org.apache.commons.text.WordUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;

public interface NameHelperMethods {

    String getFirstName();

    String getMiddleName();

    String getLastName();

    String getUsernameInternal();

    //------------------------------------------------------------------------------------------------------------------
    // NAME HELPER METHODS
    //------------------------------------------------------------------------------------------------------------------

    @Transient
    default String getFullName() {
        StringBuilder builder = new StringBuilder();
        boolean added = false;
        if (!StringUtils.isEmpty(getFirstName())) {
            builder.append(WordUtils.capitalize(getFirstName().trim().toLowerCase()));
            added = true;
        }
        if (!StringUtils.isEmpty(getMiddleName())) {
            builder.append(" ");
            builder.append(WordUtils.capitalize(getMiddleName().trim().toLowerCase()));
            added = true;
        }
        if (!StringUtils.isEmpty(getLastName())) {
            builder.append(" ");
            builder.append(WordUtils.capitalize(getLastName().trim().toLowerCase()));
            added = true;
        }
        if (!added) {
            if (!StringUtils.isEmpty(getUsernameInternal())) {
                builder.append(getUsernameInternal());
            }
        }
        return builder.toString();
    }

    default String getUsername() {
        if (!StringUtils.isEmpty(this.getUsernameInternal())) {
            return this.getUsernameInternal().trim();
        }
        return this.getFullName().replace(" ", ".").trim().toLowerCase();
    }

}
