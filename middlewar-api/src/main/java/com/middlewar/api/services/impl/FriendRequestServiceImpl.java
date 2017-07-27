package com.middlewar.api.services.impl;

import com.middlewar.api.dao.FriendRequestDao;
import com.middlewar.api.services.FriendRequestService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class FriendRequestServiceImpl extends DefaultServiceImpl<FriendRequest, FriendRequestDao> implements FriendRequestService {

    @Autowired
    private FriendRequestDao friendRequestDao;

    //@Autowired
    //private PlayerService playerService;

    @Override
    public FriendRequest create(Player requester, Player requested, String message) {

        final FriendRequest request = friendRequestDao.save(new FriendRequest(requester, requested, message));
        if (request != null) {
            requested.addRequest(request); // to be able to abort request
            requester.addRequest(request); // to be able to accept/refuse request
            // playerService.update(requested);
            // playerService.update(requester);
        }

        return request;
    }

    @Override
    public List<FriendRequest> findPlayerRequest(long playerId) {
        return friendRequestDao.findByRequestedId(playerId);
    }
}
