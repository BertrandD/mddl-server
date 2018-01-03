package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingRequirementMissingException extends ApiException {
    public BuildingRequirementMissingException() {
        super(SystemMessageId.YOU_DONT_MEET_BUILDING_REQUIREMENT);
    }
}
