package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingRequirementMissingException extends ApiException {
    public BuildingRequirementMissingException() {
        super(SystemMessageId.YOU_DONT_MEET_BUILDING_REQUIREMENT);
    }
}
