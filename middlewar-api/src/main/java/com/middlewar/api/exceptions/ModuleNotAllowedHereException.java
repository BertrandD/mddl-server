package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ModuleNotAllowedHereException extends ApiException {
    public ModuleNotAllowedHereException() {
        super(SystemMessageId.MODULE_NOT_ALLOWED_HERE);
    }
}
