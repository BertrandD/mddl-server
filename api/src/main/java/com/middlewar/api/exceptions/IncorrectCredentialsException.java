package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class IncorrectCredentialsException extends UnauthorizedException {
    public IncorrectCredentialsException() {
        super(SystemMessageId.INCORRECT_CREDENTIALS);
    }
}
