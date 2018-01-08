package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingMaxLevelReachedException extends ApiException {
    public BuildingMaxLevelReachedException() {
        super(SystemMessageId.BUILDING_MAX_LEVEL_REACHED);
    }
}
