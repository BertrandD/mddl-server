package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ItemCreationException extends ApiException {
    public ItemCreationException() {
        super(SystemMessageId.ITEM_CANNOT_CREATE);
    }
}
