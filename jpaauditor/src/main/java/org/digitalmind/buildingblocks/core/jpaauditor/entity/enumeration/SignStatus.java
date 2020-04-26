package org.digitalmind.buildingblocks.core.jpaauditor.entity.enumeration;

import lombok.Getter;

@Getter
public enum SignStatus {
    NOT_SIGNED(false),
    PARTIALLY_SIGNED(false),
    REVOKED(false),
    SIGNED(true);

    SignStatus(boolean signed) {
        this.signed = signed;
    }

    private boolean signed;
}
