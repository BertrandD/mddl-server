package com.middlewar.controllers.actions;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.SocialActionManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
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

    @RequestMapping(value = "/{id}/accept", method = RequestMethod.GET)
    public Response acceptFriendRequest(@AuthenticationPrincipal Account account, @PathVariable(value = "id") int id) {
        socialActionManager.updateFriendRequest(account.getCurrentPlayer(), id, true);
        return new Response();
    }

    @RequestMapping(value = "/{id}/refuse", method = RequestMethod.GET)
    public Response refuseFriendRequest(@AuthenticationPrincipal Account account, @PathVariable(value = "id") int id) {
        socialActionManager.updateFriendRequest(account.getCurrentPlayer(), id, false);
        return new Response();
    }
}
