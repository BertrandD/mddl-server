package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;

/**
 * @author Leboc Philippe.
 */
public interface PrivateMessageService extends DefaultService<PrivateMessage> {
    PrivateMessage create(Player author, Player receiver, String message);
}
