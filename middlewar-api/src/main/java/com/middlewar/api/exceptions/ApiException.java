package com.middlewar.api.exceptions;

/**
 * @author Bertrand
 */
public abstract class ApiException extends Exception {
    public ApiException(String message) {
        super(message);
    }
}
