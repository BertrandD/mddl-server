package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class MaximumModulesReachedException extends ApiException {
    public MaximumModulesReachedException() {
        super(SystemMessageId.MAXIMUM_MODULE_REACHED);
    }
}
