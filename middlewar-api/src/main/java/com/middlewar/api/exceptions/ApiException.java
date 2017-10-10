package com.middlewar.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Bertrand
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
