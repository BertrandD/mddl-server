package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class ItemCreationException extends ApiException {
    public ItemCreationException() {
        super(SystemMessageId.ITEM_CANNOT_CREATE);
    }
}
