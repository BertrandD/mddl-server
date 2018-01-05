package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerCreationFailedException extends ApiException {
    public PlayerCreationFailedException() {
        super(SystemMessageId.PLAYER_CREATION_FAILED);
    }
}
