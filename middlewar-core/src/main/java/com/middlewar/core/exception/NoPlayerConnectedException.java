package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class NoPlayerConnectedException extends ApiException {
    public NoPlayerConnectedException() {
        super(SystemMessageId.CHOOSE_PLAYER);
    }
}
