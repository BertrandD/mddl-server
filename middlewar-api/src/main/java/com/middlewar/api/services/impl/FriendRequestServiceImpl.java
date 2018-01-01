package com.middlewar.api.services.impl;

import com.middlewar.api.services.FriendRequestService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.repository.FriendRequestRepository;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class FriendRequestServiceImpl extends CrudServiceImpl<FriendRequest, Integer, FriendRequestRepository> implements FriendRequestService {

    public FriendRequest create(Player requester, Player requested, String message) {

        final FriendRequest request = repository.save(new FriendRequest(requester, requested, message));
        if(request == null)
            throw new RuntimeException(); // TODO create specific exception

        return request;
    }
}
