package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ItemRequirementMissingException extends ApiException {
    public ItemRequirementMissingException() {
        super(SystemMessageId.YOU_DONT_MEET_ITEM_REQUIREMENT);
    }
}
