package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BaseNotOwnedException extends ApiException {
    public BaseNotOwnedException() {
        super(SystemMessageId.BASE_NOT_OWNED);
    }
}
