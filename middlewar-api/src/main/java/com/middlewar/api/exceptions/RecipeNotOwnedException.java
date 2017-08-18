package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class RecipeNotOwnedException extends ApiException {
    public RecipeNotOwnedException() {
        super(SystemMessageId.RECIPE_NOT_OWNED);
    }
}
