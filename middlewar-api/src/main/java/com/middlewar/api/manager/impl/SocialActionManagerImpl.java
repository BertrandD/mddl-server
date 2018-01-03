package com.middlewar.api.manager.impl;

import com.middlewar.api.manager.SocialActionManager;
import com.middlewar.api.services.FriendRequestService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Leboc Philippe.
 */
public class SocialActionManagerImpl implements SocialActionManager {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Override
    public FriendRequest createFriendRequest(@NotNull Player player, int friendId, @NotNull @Size(max = 500) String message) {

        if (player.getId() == friendId) throw new RuntimeException(); // TODO: createFriendRequest specific one

        final Player friend = playerService.find(friendId);
        if (friend == null) throw new RuntimeException(); //TODO: createFriendRequest specific one

        if (player.getFriends().contains(friend)) throw new RuntimeException(); // TODO: createFriendRequest specific one

        final boolean alreadySent = player.getFriendRequests().stream().anyMatch(request -> request.getRequested().equals(friend));

        if (alreadySent) throw new RuntimeException(); // TODO

        return friendRequestService.create(player, friend, message);
    }
}
