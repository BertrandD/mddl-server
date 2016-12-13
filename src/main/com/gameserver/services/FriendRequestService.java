package com.gameserver.services;

import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.social.FriendRequest;
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

        final PlayerHolder requester = (PlayerHolder) params[0];
        final PlayerHolder requested = (PlayerHolder) params[1];
        final String message = (String) params[2];

        final FriendRequest request = new FriendRequest(requester, requested, message);
        mongoOperations.insert(request);
        return request;
    }
}
