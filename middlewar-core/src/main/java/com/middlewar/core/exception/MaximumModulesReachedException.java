package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class MaximumModulesReachedException extends ApiException {
    public MaximumModulesReachedException() {
        super(SystemMessageId.MAXIMUM_MODULE_REACHED);
    }
}
