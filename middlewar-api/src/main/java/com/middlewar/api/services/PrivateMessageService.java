package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class PrivateMessageService {

    public PrivateMessage create(Player author, Player receiver, String message) {
        PrivateMessage pm = new PrivateMessage(author, receiver, message);
        return pm;
    }
}
