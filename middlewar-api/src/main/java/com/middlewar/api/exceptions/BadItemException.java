package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BadItemException extends ApiException {
    public BadItemException() {
        super(SystemMessageId.ITEM_NOT_FOUND);
    }
}
