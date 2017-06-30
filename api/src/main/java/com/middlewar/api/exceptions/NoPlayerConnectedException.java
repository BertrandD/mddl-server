package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class NoPlayerConnectedException extends ApiException {
    public NoPlayerConnectedException() {
        super(SystemMessageId.CHOOSE_PLAYER);
    }
}
