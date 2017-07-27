package com.middlewar.api.services.impl;

import com.middlewar.api.dao.PrivateMessageDao;
import com.middlewar.api.services.PrivateMessageService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class PrivateMessageServiceImpl extends DefaultServiceImpl<PrivateMessage, PrivateMessageDao> implements PrivateMessageService {

    @Autowired
    private PrivateMessageDao privateMessageDao;

    @Override
    public PrivateMessage create(Player author, Player receiver, String message) {
        return privateMessageDao.save(new PrivateMessage(author, receiver, message));
    }
}
