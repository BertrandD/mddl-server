package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class BadItemException extends ApiException {
    public BadItemException() {
        super(SystemMessageId.ITEM_NOT_FOUND);
    }
}
