package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class RecipeCreationFailedException extends ApiException {
    public RecipeCreationFailedException() {
        super(SystemMessageId.RECIPE_CREATION_FAILED);
    }
}
