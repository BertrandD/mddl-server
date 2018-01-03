package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class NotEnoughModulesException extends ApiException {
    public NotEnoughModulesException() {
        super(SystemMessageId.NOT_ENOUGH_MODULES);
    }
}
