package com.middlewar.core.exception;

/**
 * @author Bertrand
 */
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
