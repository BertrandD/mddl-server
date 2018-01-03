package com.middlewar.core.exceptions;

/**
 * @author Bertrand
 */
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
