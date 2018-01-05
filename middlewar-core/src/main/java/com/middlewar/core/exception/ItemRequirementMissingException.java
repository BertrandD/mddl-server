package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class ItemRequirementMissingException extends ApiException {
    public ItemRequirementMissingException() {
        super(SystemMessageId.YOU_DONT_MEET_ITEM_REQUIREMENT);
    }
}
