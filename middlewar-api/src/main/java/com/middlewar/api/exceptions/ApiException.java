package com.middlewar.api.exceptions;

/**
 * @author Bertrand
 */
public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
