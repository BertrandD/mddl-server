package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class FriendRequestService implements DefaultService<FriendRequest>{
    private int nextId = 0;

    public FriendRequest create(Player requester, Player requested, String message) {

        final FriendRequest request = new FriendRequest(requester, requested, message);
        request.setId(nextId());
        requested.addRequest(request); // to be able to abort request
        requester.addRequest(request); // to be able to accept/refuse request

        return request;
    }

    @Override
    public void delete(FriendRequest o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FriendRequest findOne(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextId() {
        return ++nextId;
    }
}
