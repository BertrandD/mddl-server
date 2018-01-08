package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class MaxPlayerCreationReachedException extends ApiException {
    public MaxPlayerCreationReachedException() {
        super(SystemMessageId.MAXIMUM_PLAYER_CREATION_REACHED);
    }
}
