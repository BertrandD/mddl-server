package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class ShipCreationFailedException extends ApiException {
    public ShipCreationFailedException() {
        super(SystemMessageId.SHIP_CREATION_FAILED);
    }
}
