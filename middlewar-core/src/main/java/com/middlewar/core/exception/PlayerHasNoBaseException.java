package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerHasNoBaseException extends ApiException {
    public PlayerHasNoBaseException() {
        super(SystemMessageId.PLAYER_HAS_NO_BASE);
    }
}
