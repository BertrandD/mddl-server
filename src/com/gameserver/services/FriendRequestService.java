package com.gameserver.services;

import com.gameserver.model.FriendRequest;
import com.gameserver.model.Player;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class FriendRequestService extends DatabaseService<FriendRequest> {

    protected FriendRequestService() {
        super(FriendRequest.class);
    }

    @Override
    public FriendRequest create(Object... params) {
        if(params.length != 3) return null;

        final Player requester = (Player) params[0];
        final Player requested = (Player) params[1];
        final String message = (String) params[2];

        final FriendRequest request = new FriendRequest(requester, requested, message);
        mongoOperations.insert(request);
        return request;
    }
}
