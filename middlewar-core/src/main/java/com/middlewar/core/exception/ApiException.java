package com.middlewar.core.exception;

/**
 * @author Bertrand
 */
public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
