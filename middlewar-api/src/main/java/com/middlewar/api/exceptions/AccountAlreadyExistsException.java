package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class AccountAlreadyExistsException extends ApiException {
    public AccountAlreadyExistsException() {
        super(SystemMessageId.ACCOUNT_ALREADY_EXIST);
    }
}
