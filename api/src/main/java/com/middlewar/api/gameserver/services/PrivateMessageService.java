package com.gameserver.services;

import com.gameserver.holders.PlayerHolder;
import com.gameserver.model.social.PrivateMessage;

/**
 * @author Leboc Philippe.
 */
public interface PrivateMessageService extends DefaultService<PrivateMessage> {
    PrivateMessage create(PlayerHolder author, PlayerHolder receiver, String message);
}
