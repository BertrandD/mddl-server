package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingAlreadyExistsException extends ApiException {
    public BuildingAlreadyExistsException() {
        super(SystemMessageId.BUILDING_ALREADY_EXIST);
    }
}
