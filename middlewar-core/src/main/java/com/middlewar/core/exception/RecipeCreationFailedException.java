package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class RecipeCreationFailedException extends ApiException {
    public RecipeCreationFailedException() {
        super(SystemMessageId.RECIPE_CREATION_FAILED);
    }
}
