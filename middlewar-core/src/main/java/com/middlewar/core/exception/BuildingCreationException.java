package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingCreationException extends ApiException {
    public BuildingCreationException() {
        super(SystemMessageId.BUILDING_CANNOT_CREATE);
    }
}
