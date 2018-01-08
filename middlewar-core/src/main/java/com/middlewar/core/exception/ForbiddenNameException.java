package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class ForbiddenNameException extends ApiException {
    public ForbiddenNameException() {
        super(SystemMessageId.FORBIDDEN_NAME);
    }
}
