package com.middlewar.api.services.impl;

import com.middlewar.api.dao.FriendRequestDao;
import com.middlewar.api.services.PlayerService;
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

    @Autowired
    private PlayerService playerService;

    @Override
    public FriendRequest create(Player requester, Player requested, String message) {

        final FriendRequest request = friendRequestDao.insert(new FriendRequest(new PlayerHolder(requester), new PlayerHolder(requested), message));
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
    public FriendRequest create(PlayerHolder requester, PlayerHolder requested, String message) {

        final FriendRequest request = friendRequestDao.insert(new FriendRequest(requester, requested, message));
        if(request != null) {
            final Player playerRequester = playerService.findOne(requester.getId());
            final Player playerRequested = playerService.findOne(requested.getId());

            if(playerRequester == null || playerRequested == null) return null;

            playerRequested.addRequest(request); // to be able to abort request
            playerRequester.addRequest(request); // to be able to accept/refuse request
            playerService.update(playerRequested);
            playerService.update(playerRequester);
        }

        return request;
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
