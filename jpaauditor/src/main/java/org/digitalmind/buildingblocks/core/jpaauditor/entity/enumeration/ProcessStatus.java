package org.digitalmind.buildingblocks.core.jpaauditor.entity.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum ProcessStatus {

    CREATED(true, false, Arrays.asList(), "The process has been created but not validated yet"),
    LOADED(true, false, Arrays.asList(CREATED), "The process referenced documents were loaded"),
    VALIDATED(true, false, Arrays.asList(CREATED, LOADED), "The process has been validated but not activated yet"),
    ACTIVE(true, false, Arrays.asList(VALIDATED), "The process is runing and expecting signatures"),
    ERROR(false, false, Arrays.asList(ACTIVE, CREATED, VALIDATED), "The process is in error state"),

    CANCELLED(false, false, Arrays.asList(CREATED, LOADED, VALIDATED, ACTIVE), "The process has been cancelled before completing signature process"),
    COMPLETED(false, true, Arrays.asList(ACTIVE), "The process has been completed with success"),
    REVOKED(false, false, Arrays.asList(COMPLETED), "The process and associated documents signatures were revoked after a successfully sign"),
    EXPIRED(false, false, Arrays.asList(CREATED, VALIDATED, ACTIVE), "The process was finished without completing signing documents");

    private String description;
    private boolean active;
    private boolean success;
    private List<ProcessStatus> previousStatuses;

    ProcessStatus(boolean active, boolean success, List<ProcessStatus> previousStatuses, String description) {
        this.description = description;
        this.active = active;
        this.success = success;
        this.previousStatuses = Collections.unmodifiableList(previousStatuses);
    }

    public boolean allowChangeTo(ProcessStatus status) {
        return status.getPreviousStatuses().contains(this);
    }

}
