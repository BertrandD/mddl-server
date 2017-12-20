package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class NotEnoughSlotsException extends ApiException {
    public NotEnoughSlotsException() {
        super(SystemMessageId.NOT_ENOUGH_SLOTS);
    }
}
