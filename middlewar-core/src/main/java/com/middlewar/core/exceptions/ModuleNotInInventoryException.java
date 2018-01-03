package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class ModuleNotInInventoryException extends ApiException {
    public ModuleNotInInventoryException() {
        super(SystemMessageId.MODULE_NOT_IN_INVENTORY);
    }
}
