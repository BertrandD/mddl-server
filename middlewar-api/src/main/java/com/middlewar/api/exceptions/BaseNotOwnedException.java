package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BaseNotOwnedException extends ApiException {
    public BaseNotOwnedException() {
        super(SystemMessageId.BASE_NOT_OWNED);
    }
}
