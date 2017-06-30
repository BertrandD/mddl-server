package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BaseNotFoundException extends ApiException {
    public BaseNotFoundException() {
        super(SystemMessageId.BASE_NOT_FOUND);
    }
}
