package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ShipCreationFailedException extends ApiException {
    public ShipCreationFailedException() {
        super(SystemMessageId.SHIP_CREATION_FAILED);
    }
}
