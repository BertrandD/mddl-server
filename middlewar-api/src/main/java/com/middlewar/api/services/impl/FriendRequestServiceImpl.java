package com.middlewar.api.services.impl;

import com.middlewar.api.services.FriendRequestService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.repository.FriendRequestRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author LEBOC Philippe
 */
@Service
@Validated
public class FriendRequestServiceImpl extends CrudServiceImpl<FriendRequest, Integer, FriendRequestRepository> implements FriendRequestService {

    @Override
    public FriendRequest create(@NotNull Player requester, @NotNull Player requested, @NotEmpty String message) {

        final FriendRequest request = repository.save(new FriendRequest(requester, requested, message));
        if(request == null)
            throw new RuntimeException(); // TODO createFriendRequest specific exception

        return request;
    }
}
