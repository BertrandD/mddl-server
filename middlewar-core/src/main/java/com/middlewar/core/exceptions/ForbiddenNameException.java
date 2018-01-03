package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ForbiddenNameException extends ApiException {
    public ForbiddenNameException() {
        super(SystemMessageId.FORBIDDEN_NAME);
    }
}
