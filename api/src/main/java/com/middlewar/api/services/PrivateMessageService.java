package com.middlewar.api.services;

import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.social.PrivateMessage;

/**
 * @author Leboc Philippe.
 */
public interface PrivateMessageService extends DefaultService<PrivateMessage> {
    PrivateMessage create(PlayerHolder author, PlayerHolder receiver, String message);
}
