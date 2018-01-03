package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BaseCreationException extends ApiException {
    public BaseCreationException() {
        super(SystemMessageId.BASE_CANNOT_CREATE);
    }
}
