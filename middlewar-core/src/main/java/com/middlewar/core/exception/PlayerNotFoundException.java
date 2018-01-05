package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerNotFoundException extends ApiException {
    public PlayerNotFoundException() {
        super(SystemMessageId.PLAYER_NOT_FOUND);
    }
}
