package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class NotEnoughModulesException extends ApiException {
    public NotEnoughModulesException() {
        super(SystemMessageId.NOT_ENOUGH_MODULES);
    }
}
