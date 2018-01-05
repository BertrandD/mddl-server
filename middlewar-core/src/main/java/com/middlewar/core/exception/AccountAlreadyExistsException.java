package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class AccountAlreadyExistsException extends ApiException {
    public AccountAlreadyExistsException() {
        super(SystemMessageId.ACCOUNT_ALREADY_EXIST);
    }
}
