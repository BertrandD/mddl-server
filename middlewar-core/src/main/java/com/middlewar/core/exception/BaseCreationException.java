package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BaseCreationException extends ApiException {
    public BaseCreationException() {
        super(SystemMessageId.BASE_CANNOT_CREATE);
    }
}
