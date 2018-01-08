package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class RecipeNotFoundException extends ApiException {
    public RecipeNotFoundException() {
        super(SystemMessageId.RECIPE_NOT_FOUND);
    }
}
