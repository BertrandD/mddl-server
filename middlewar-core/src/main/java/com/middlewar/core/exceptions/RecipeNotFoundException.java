package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class RecipeNotFoundException extends ApiException {
    public RecipeNotFoundException() {
        super(SystemMessageId.RECIPE_NOT_FOUND);
    }
}
