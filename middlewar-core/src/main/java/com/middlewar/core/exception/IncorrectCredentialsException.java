package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class IncorrectCredentialsException extends UnauthorizedException {
    public IncorrectCredentialsException() {
        super(SystemMessageId.INCORRECT_CREDENTIALS);
    }
}
