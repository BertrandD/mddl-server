package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class UsernameNotFoundException extends UnauthorizedException {
    public UsernameNotFoundException() {
        super(SystemMessageId.USERNAME_NOT_FOUND);
    }
}
