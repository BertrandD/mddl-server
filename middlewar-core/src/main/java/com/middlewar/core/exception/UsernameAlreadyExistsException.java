package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException() {
        super(SystemMessageId.USERNAME_ALREADY_EXIST);
    }
}
