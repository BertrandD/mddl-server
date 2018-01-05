package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class ModuleNotInInventoryException extends ApiException {
    public ModuleNotInInventoryException() {
        super(SystemMessageId.MODULE_NOT_IN_INVENTORY);
    }
}
