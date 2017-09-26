package com.middlewar.api.services;

import com.middlewar.api.dao.PrivateMessageDAO;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class PrivateMessageService implements DefaultService<PrivateMessage> {

    @Autowired
    private PrivateMessageDAO privateMessageDao;

    public PrivateMessage create(Player author, Player receiver, String message) {
        PrivateMessage pm = new PrivateMessage(author, receiver, message);
        pm.setId(nextId());
        privateMessageDao.add(pm);
        return pm;
    }

    @Override
    public void delete(PrivateMessage o) {
        privateMessageDao.remove(o);
    }

    public PrivateMessage findOne(int pmId) {
        return privateMessageDao.getById(pmId);
    }

    @Override
    public int nextId() {
        return privateMessageDao.count() + 1;
    }
}
