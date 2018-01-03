package com.middlewar.core.exceptions;

/**
 * @author Bertrand
 */
public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
