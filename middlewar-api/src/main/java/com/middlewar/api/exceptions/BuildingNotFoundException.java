package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingNotFoundException extends ApiException {
    public BuildingNotFoundException() {
        super(SystemMessageId.BUILDING_NOT_FOUND);
    }
}
