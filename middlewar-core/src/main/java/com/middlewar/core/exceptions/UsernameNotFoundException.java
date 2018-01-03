package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class UsernameNotFoundException extends UnauthorizedException {
    public UsernameNotFoundException() {
        super(SystemMessageId.USERNAME_NOT_FOUND);
    }
}
