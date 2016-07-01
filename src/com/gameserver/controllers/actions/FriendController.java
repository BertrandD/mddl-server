package com.gameserver.controllers.actions;

import com.auth.Account;
import com.gameserver.holders.PlayerHolder;
import com.gameserver.model.FriendRequest;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.services.FriendRequestService;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class FriendController {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FriendRequestService friendRequestService;

    @RequestMapping(value = "/friend/request", method = RequestMethod.POST)
    public JsonResponse sendFriendRequest(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "playerId") String friendId, @RequestParam(value = "message") String message) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        if(player.getId().equals(friendId)) return new JsonResponse(JsonResponseType.ERROR, "You cannot request yourself."); // TODO: SysMsg

        final Player friend = playerService.findOne(friendId);
        if(friend == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        if(player.getFriends().contains(friend)) return new JsonResponse(JsonResponseType.ERROR, friend.getName() + " is already in your friend list.");

        int counter = 0;
        boolean allreadySent = false;
        while(!allreadySent && counter < player.getFriendRequests().size()) {
            final FriendRequest frequest = player.getFriendRequests().get(counter);
            if(frequest != null && frequest.getRequested().equals(friend))
                allreadySent = true;
            counter++;
        }

        if(allreadySent) return new JsonResponse(JsonResponseType.ERROR, "You have already sent a friend request to " + friend.getName());

        final FriendRequest request = friendRequestService.create(player, friend, message);
        if(request == null) return new JsonResponse(JsonResponseType.ERROR, "Failed to send friend request.");

        player.addRequest(request); // to be able to abort request
        friend.addRequest(request); // to be able to accept/refuse request
        playerService.update(friend);
        playerService.update(player);

        return new JsonResponse("Friend request sent successfully !"); // TODO: SysMsg
    }

    @RequestMapping(value = "/friend/accept/{requestId}", method = RequestMethod.GET)
    public JsonResponse acceptFriend(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "requestId") String requestId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getFriendRequests().stream().filter(k -> k.getId().equals(requestId)).findFirst().orElse(null);
        if(request == null) return new JsonResponse(JsonResponseType.ERROR, "Request doesn't exist.");

        if(player == request.getRequester()) return new JsonResponse(JsonResponseType.ERROR, "Invalid request");

        final Player friend = request.getRequester();
        if(friend == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        if(!player.addFriend(new PlayerHolder(friend.getId(), friend.getName()))) return new JsonResponse(JsonResponseType.ERROR, friend.getName() + " is already in your friend list."); // TODO: SysMsg

        friend.addFriend(new PlayerHolder(player.getId(), player.getName()));
        friend.getFriendRequests().remove(request);
        player.getFriendRequests().remove(request);

        friendRequestService.delete(request);
        playerService.update(friend);
        playerService.update(player);

        return new JsonResponse(friend.getName() + " added successfully to your friend list !"); // TODO: SysMsg
    }

    @RequestMapping(value = "/friend/refuse/{requestId}", method = RequestMethod.GET)
    public JsonResponse refuseFriend(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "requestId") String requestId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getFriendRequests().stream().filter(k -> k.getId().equals(requestId)).findFirst().orElse(null);
        if(request == null) return new JsonResponse(JsonResponseType.ERROR, "Request doesn't exist.");

        final Player friend = request.getRequester();
        if(friend == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        friend.getFriendRequests().remove(request);
        player.getFriendRequests().remove(request);

        friendRequestService.delete(request);
        playerService.update(friend);
        playerService.update(player);

        return new JsonResponse(friend.getName() + " has been refused !"); // TODO: SysMsg
    }
}
