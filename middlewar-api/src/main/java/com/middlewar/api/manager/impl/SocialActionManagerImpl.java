package com.middlewar.api.manager.impl;

import com.middlewar.api.manager.SocialActionManager;
import com.middlewar.api.services.FriendRequestService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.exception.FriendAlreadyInFriendListException;
import com.middlewar.core.exception.FriendRequestAlreadySentException;
import com.middlewar.core.exception.FriendRequestForbiddenAccessException;
import com.middlewar.core.exception.FriendRequestNotFoundException;
import com.middlewar.core.exception.FriendRequestToSelfException;
import com.middlewar.core.exception.PlayerIsAlreadyFriendException;
import com.middlewar.core.exception.PlayerNotFoundException;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.middlewar.core.predicate.FriendRequestPredicate.requestedIs;

/**
 * @author Leboc Philippe.
 */
@Service
@Validated
public class SocialActionManagerImpl implements SocialActionManager {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Override
    public FriendRequest createFriendRequest(@NotNull Player player, int friendId, @NotNull @Size(max = 500) String message) {

        if (player.getId() == friendId) throw new FriendRequestToSelfException();

        final Player friend = playerService.find(friendId);
        if (friend == null) throw new PlayerNotFoundException();

        if (player.getFriends().contains(friend)) throw new PlayerIsAlreadyFriendException();

        final boolean alreadySent = player.getFriendRequests().stream().anyMatch(requestedIs(friend));

        if (alreadySent) throw new FriendRequestAlreadySentException();

        final FriendRequest request = friendRequestService.create(player, friend, message);

        player.getFriendRequests().add(request);
        friend.getFriendRequests().add(request);

        playerService.updateAsync(player);
        playerService.updateAsync(friend);

        return request;
    }

    @Override
    public void updateFriendRequest(@NotNull Player player, int requestId, boolean accept) {

        final FriendRequest request = friendRequestService.find(requestId);
        if (request == null) throw new FriendRequestNotFoundException();

        final Player requester = request.getRequester();
        final Player requested = request.getRequested();

        if(!player.equals(requested))
            throw new FriendRequestForbiddenAccessException();

        if(accept) {
            if(requested.getFriends().contains(requester))
                throw new FriendAlreadyInFriendListException();

            requester.getFriends().add(requested);
            requested.getFriends().add(requester);
        }

        requester.getFriendRequests().remove(request);
        requested.getFriendRequests().remove(request);

        playerService.update(requested);
        playerService.updateAsync(requester);

        friendRequestService.deleteAsync(request);
    }
}
