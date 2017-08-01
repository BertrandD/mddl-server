package com.middlewar.controllers.actions;

import com.middlewar.api.services.FriendRequestService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/friend", produces = "application/json")
public class FriendController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FriendRequestService friendRequestService;

    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public Response showFriendRequest(@AuthenticationPrincipal Account pAccount) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final List<FriendRequest> requests = friendRequestService.findPlayerRequest(player.getId());
        return new Response<>(requests);
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public Response sendFriendRequest(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "friendId") Long friendId, @RequestParam(value = "message") String message) {
//        Assert.hasLength(friendId, "Invalid parameter friendId.");
        Assert.hasLength(message, "Empty message.");

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        if (player.getId() == (friendId))
            return new Response<>(JsonResponseType.ERROR, SystemMessageId.YOU_CANNOT_REQUEST_YOURSELF);

        final Player friend = playerService.findOne(friendId);
        if (friend == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        // TODO SysMsg
        if (player.getFriends().contains(friend))
            return new Response<>(JsonResponseType.ERROR, friend.getName() + " is already in your friend list.");

        int counter = 0;
        boolean allreadySent = false;
        while (!allreadySent && counter < player.getEmittedFriendRequests().size()) {
            final FriendRequest frequest = player.getEmittedFriendRequests().get(counter);
            if (frequest != null && frequest.getRequested().is(friend))
                allreadySent = true;
            counter++;
        }

        if (allreadySent)
            return new Response<>(JsonResponseType.ERROR, "You have already sent a friend request to " + friend.getName());

        final FriendRequest request = friendRequestService.create(player, friend, message);
        if (request == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.FAILED_TO_SEND_FRIEND_REQUEST);

        return new Response<>(player); // TODO: send only the new friendrequest instead of Player !
    }

    @RequestMapping(value = "/accept/{requestId}", method = RequestMethod.GET)
    public Response acceptFriend(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "requestId") Long requestId) {
//        Assert.hasLength(requestId, "Invalid parameter requestId");

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getReceivedFriendRequests().stream().filter(k -> k.getId() == (requestId)).findFirst().orElse(null);
        if (request == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.FRIEND_REQUEST_DOESNT_EXIST);

        final Player friend = playerService.findOne(request.getRequester().getId());
        if (friend == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        if (!player.addFriend(friend))
            return new Response<>(JsonResponseType.ERROR, friend.getName() + " is already in your friend list."); // TODO: SysMsg

        friend.addFriend(player);

        friend.getEmittedFriendRequests().remove(request);
        player.getReceivedFriendRequests().remove(request);

        playerService.update(friend);
        playerService.update(player);
        friendRequestService.delete(request);

        return new Response<>(player);
    }

    @RequestMapping(value = "/refuse/{requestId}", method = RequestMethod.GET)
    public Response refuseFriend(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "requestId") Long requestId) {
//        Assert.hasLength(requestId, "Invalid parameter requestId");

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getReceivedFriendRequests().stream().filter(k -> k.getId() == (requestId)).findFirst().orElse(null);
        if (request == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.FRIEND_REQUEST_DOESNT_EXIST);

        final Player friend = playerService.findOne(request.getRequester().getId());
        if (friend == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        friend.getEmittedFriendRequests().remove(request);
        player.getEmittedFriendRequests().remove(request);

        friendRequestService.delete(request);
        playerService.update(friend);
        playerService.update(player);

        return new Response<>(player); // TODO: SysMsg
    }
}