package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingMaxLevelReachedException extends ApiException {
    public BuildingMaxLevelReachedException() {
        super(SystemMessageId.BUILDING_MAX_LEVEL_REACHED);
    }
}
