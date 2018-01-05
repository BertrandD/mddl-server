package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BaseNotFoundException extends ApiException {
    public BaseNotFoundException() {
        super(SystemMessageId.BASE_NOT_FOUND);
    }
}
