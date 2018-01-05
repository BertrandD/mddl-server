package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingNotFoundException extends ApiException {
    public BuildingNotFoundException() {
        super(SystemMessageId.BUILDING_NOT_FOUND);
    }
}
