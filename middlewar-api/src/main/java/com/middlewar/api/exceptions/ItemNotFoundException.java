package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ItemNotFoundException extends ApiException {
    public ItemNotFoundException() {
        super(SystemMessageId.ITEM_NOT_FOUND);
    }
}
