package com.gameserver.services;

import com.gameserver.model.FriendRequest;
import com.gameserver.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class FriendRequestService {

    @Autowired
    private MongoOperations mongoOperations;

    public FriendRequest create(Player requester, Player requested, String message) {
        if(requester == null || message == null) return null;

        final FriendRequest request = new FriendRequest(requester, requested, message);
        mongoOperations.save(request);
        return request;
    }

    @Async
    public void delete(FriendRequest request) {
        if(request == null) return;
        mongoOperations.remove(request);
    }
}
