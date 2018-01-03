package com.middlewar.core.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class PlayerHasNoBaseException extends ApiException {
    public PlayerHasNoBaseException() {
        super(SystemMessageId.PLAYER_HAS_NO_BASE);
    }
}
