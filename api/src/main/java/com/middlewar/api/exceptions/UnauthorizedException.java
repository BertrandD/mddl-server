package com.middlewar.api.exceptions;

/**
 * @author Bertrand
 */
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
