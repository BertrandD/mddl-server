package com.middlewar.core.exceptions;

/**
 * @author Bertrand
 */
public class BuildingNotFoundException extends ApiException {
    public BuildingNotFoundException() {
        super(SystemMessageId.BUILDING_NOT_FOUND);
    }
}
