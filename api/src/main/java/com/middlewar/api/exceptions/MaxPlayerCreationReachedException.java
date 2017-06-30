package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class MaxPlayerCreationReachedException extends ApiException {
    public MaxPlayerCreationReachedException() {
        super(SystemMessageId.MAXIMUM_PLAYER_CREATION_REACHED);
    }
}
