package com.gameserver.services.impl;

import com.gameserver.dao.PrivateMessageDao;
import com.gameserver.holders.PlayerHolder;
import com.gameserver.model.social.PrivateMessage;
import com.gameserver.services.PrivateMessageService;
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
    public PrivateMessage create(PlayerHolder author, PlayerHolder receiver, String message) {
        return privateMessageDao.insert(new PrivateMessage(author, receiver, message));
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
    public void clearAll() {
        privateMessageDao.deleteAll();
    }
}
