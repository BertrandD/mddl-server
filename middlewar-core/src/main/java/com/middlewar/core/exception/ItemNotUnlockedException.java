package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class ItemNotUnlockedException extends ApiException {
    public ItemNotUnlockedException() {
        super(SystemMessageId.ITEM_NOT_UNLOCKED);
    }
}
