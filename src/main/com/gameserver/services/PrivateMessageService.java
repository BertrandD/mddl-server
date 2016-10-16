package com.gameserver.services;

import com.gameserver.holders.PlayerHolder;
import com.gameserver.model.social.PrivateMessage;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class PrivateMessageService extends DatabaseService<PrivateMessage> {

    protected PrivateMessageService() {
        super(PrivateMessage.class);
    }

    @Override
    public PrivateMessage create(Object... params) {
        if(params.length != 3) return null;
        final PlayerHolder author = (PlayerHolder) params[0];
        final PlayerHolder receiver = (PlayerHolder) params[1];
        final String message = (String) params[2];

        final PrivateMessage privateMessage = new PrivateMessage(author, receiver, message);
        mongoOperations.insert(privateMessage);
        return privateMessage;
    }
}
