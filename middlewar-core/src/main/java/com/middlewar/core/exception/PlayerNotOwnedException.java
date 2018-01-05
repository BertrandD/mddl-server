package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerNotOwnedException extends ApiException {
    public PlayerNotOwnedException() {
        super(SystemMessageId.PLAYER_NOT_OWNED);
    }
}
