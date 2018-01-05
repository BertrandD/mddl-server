package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class ModuleNotAllowedHereException extends ApiException {
    public ModuleNotAllowedHereException() {
        super(SystemMessageId.MODULE_NOT_ALLOWED_HERE);
    }
}
