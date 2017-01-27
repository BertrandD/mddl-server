package com.middlewar.api.services.impl;

import com.middlewar.api.dao.FriendRequestDao;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.api.services.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestDao friendRequestDao;

    public FriendRequest create(Player requester, Player requested, String message) {
        return friendRequestDao.insert(new FriendRequest(new PlayerHolder(requester), new PlayerHolder(requested), message));
    }

    @Override
    public FriendRequest create(PlayerHolder requester, PlayerHolder requested, String message) {
        return friendRequestDao.insert(new FriendRequest(requester, requested, message));
    }

    @Override
    public FriendRequest findOne(String id) {
        return friendRequestDao.findOne(id);
    }

    @Override
    public List<FriendRequest> findAll() {
        return friendRequestDao.findAll();
    }

    @Override
    public void update(FriendRequest object) {
        friendRequestDao.save(object);
    }

    @Override
    public void remove(FriendRequest object) {
        friendRequestDao.delete(object);
    }

    @Override
    public void clearAll() {
        friendRequestDao.deleteAll();
    }
}
