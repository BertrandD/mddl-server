package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerNotFoundException extends ApiException {
    public PlayerNotFoundException() {
        super(SystemMessageId.PLAYER_NOT_FOUND);
    }
}
