package com.middlewar.controllers.actions;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.SocialActionManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.request.FriendRequestCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/friend", produces = "application/json")
public class FriendController {

    @Autowired
    private SocialActionManager socialActionManager;

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public Response sendFriendRequest(@AuthenticationPrincipal Account account, @RequestBody FriendRequestCreationRequest request) {
        final Player player = account.getCurrentPlayer();
        return new Response(socialActionManager.createFriendRequest(player, request.getFriendId(), request.getMessage()));
    }

    @RequestMapping(value = Route.REQUEST_ACCEPT, method = RequestMethod.GET)
    public Response acceptFriend(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "requestId") Long requestId) {

        final Player player = playerService.find(pAccount.getCurrentPlayerId());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getFriendRequests().stream().filter(k -> k.getId() == (requestId)).findFirst().orElse(null);
        if (request == null) return new Response(SystemMessageId.FRIEND_REQUEST_DOESNT_EXIST);

        final Player friend = playerService.find(request.getRequester().getId());
        if (friend == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        if (!player.addFriend(friend))
            return new Response(friend.getName() + " is already in your friend list."); // TODO: SysMsg

        friend.addFriend(player);

        friend.getFriendRequests().remove(request);
        player.getFriendRequests().remove(request);

        return new Response(player);
    }

    @RequestMapping(value = Route.REQUEST_REFUSE, method = RequestMethod.GET)
    public Response refuseFriend(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "requestId") Long requestId) {

        final Player player = playerService.find(pAccount.getCurrentPlayerId());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getFriendRequests().stream().filter(k -> k.getId() == (requestId)).findFirst().orElse(null);
        if (request == null) return new Response(SystemMessageId.FRIEND_REQUEST_DOESNT_EXIST);

        final Player friend = playerService.find(request.getRequester().getId());
        if (friend == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        friend.getFriendRequests().remove(request);
        player.getFriendRequests().remove(request);

        return new Response(player); // TODO: SysMsg
    }
}
