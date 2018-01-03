package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerCreationFailedException extends ApiException {
    public PlayerCreationFailedException() {
        super(SystemMessageId.PLAYER_CREATION_FAILED);
    }
}
