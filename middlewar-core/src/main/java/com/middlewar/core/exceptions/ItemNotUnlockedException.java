package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ItemNotUnlockedException extends ApiException {
    public ItemNotUnlockedException() {
        super(SystemMessageId.ITEM_NOT_UNLOCKED);
    }
}
