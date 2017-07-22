package com.middlewar.api.services.impl;

import com.middlewar.api.dao.PrivateMessageDao;
import com.middlewar.api.services.PrivateMessageService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

    @Autowired
    private PrivateMessageDao privateMessageDao;

    @Override
    public PrivateMessage create(Player author, Player receiver, String message) {
        return privateMessageDao.save(new PrivateMessage(author, receiver, message));
    }

    @Override
    public PrivateMessage findOne(String id) {
        return privateMessageDao.findOne(id);
    }

    @Override
    public List<PrivateMessage> findAll() {
        return privateMessageDao.findAll();
    }

    @Override
    public void update(PrivateMessage object) {
        privateMessageDao.save(object);
    }

    @Override
    public void remove(PrivateMessage object) {
        privateMessageDao.delete(object);
    }

    @Override
    public void deleteAll() {
        privateMessageDao.deleteAll();
    }
}
