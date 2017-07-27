package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingCreationException extends ApiException {
    public BuildingCreationException() {
        super(SystemMessageId.BUILDING_CANNOT_CREATE);
    }
}
