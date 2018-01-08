package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingAlreadyExistsException extends ApiException {
    public BuildingAlreadyExistsException() {
        super(SystemMessageId.BUILDING_ALREADY_EXIST);
    }
}
