package com.middlewar.api.services.impl;

import com.middlewar.api.dao.FriendRequestDao;
import com.middlewar.api.services.FriendRequestService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
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

    @Autowired
    private PlayerService playerService;

    @Override
    public FriendRequest create(Player requester, Player requested, String message) {

        final FriendRequest request = friendRequestDao.save(new FriendRequest(requester, requested, message));
        if(request != null) {
            requested.addRequest(request); // to be able to abort request
            requester.addRequest(request); // to be able to accept/refuse request
            playerService.update(requested);
            playerService.update(requester);
        }

        return request;
    }

    @Override
    public List<FriendRequest> findPlayerRequest(String playerId) {
        return friendRequestDao.findByRequestedId(playerId);
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
    public void deleteAll() {
        friendRequestDao.deleteAll();
    }


}
