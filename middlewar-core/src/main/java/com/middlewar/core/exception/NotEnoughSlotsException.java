package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class NotEnoughSlotsException extends ApiException {
    public NotEnoughSlotsException() {
        super(SystemMessageId.NOT_ENOUGH_SLOTS);
    }
}
