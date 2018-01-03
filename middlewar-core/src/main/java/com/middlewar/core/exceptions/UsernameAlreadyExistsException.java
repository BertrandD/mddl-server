package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class UsernameAlreadyExistsException extends ApiException {
    public UsernameAlreadyExistsException() {
        super(SystemMessageId.USERNAME_ALREADY_EXIST);
    }
}
