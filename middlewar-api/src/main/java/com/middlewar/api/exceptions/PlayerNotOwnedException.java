package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerNotOwnedException extends ApiException {
    public PlayerNotOwnedException() {
        super(SystemMessageId.PLAYER_NOT_OWNED);
    }
}
