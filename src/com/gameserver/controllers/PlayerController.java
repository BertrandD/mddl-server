package com.gameserver.controllers;

import com.auth.Account;
import com.auth.AccountService;
import com.config.Config;
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
public class PlayerController {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FriendRequestService friendRequestService;

    @RequestMapping(value = "/me/player", method = RequestMethod.GET)
    public JsonResponse players(@AuthenticationPrincipal Account pAccount){
        final Account account = accountService.findOne(pAccount.getId());
        return new JsonResponse(playerService.findByAccount(account));
    }

    @RequestMapping(value = "/me/player/{id}", method = RequestMethod.GET)
    public JsonResponse player(@AuthenticationPrincipal Account account, @PathVariable("id") String id){
        final Player player = playerService.findOne(id);
        if(player == null) return new JsonResponse(account.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        return new JsonResponse(player);
    }

    @RequestMapping(value = "/player", method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name) {
        if(account.getPlayers().size() >= Config.MAX_PLAYER_IN_ACCOUNT) return new JsonResponse("Maximum player creation reached !"); // TODO: SysMsg

        if(playerService.findOneByName(name) != null) return new JsonResponse(account.getLang(), SystemMessageId.USERNAME_ALREADY_EXIST);

        // Check if name is forbidden (Like 'fuck', 'admin', ...)
        if (Config.FORBIDDEN_NAMES.length > 1)
        {
            for (String st : Config.FORBIDDEN_NAMES)
            {
                if (name.toLowerCase().contains(st.toLowerCase()))
                {
                    logger.info("Player creation failed : Forbidden name.");
                    return new JsonResponse(account.getLang(), SystemMessageId.FORBIDDEN_NAME);
                }
            }
        }

        final Account playerAccount = accountService.findOne(account.getId());
        final Player player = playerService.create(account, name);

        account.addPlayer(player.getId());
        account.setCurrentPlayer(player.getId());

        playerAccount.addPlayer(player.getId());
        playerAccount.setCurrentPlayer(player.getId());

        logger.info("Player creation success : "+ player.getName() +".");

        accountService.update(playerAccount);
        return new JsonResponse(player);
    }

    @RequestMapping(value = "/friend/request", method = RequestMethod.POST)
    public JsonResponse sendFriendRequest(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "playerId") String friendId, @RequestParam(value = "message") String message) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        if(player.getId().equals(friendId)) return new JsonResponse("You cannot request yourself."); // TODO: SysMsg

        final Player friend = playerService.findOne(friendId);
        if(friend == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        if(player.getFriends().contains(friend)) return new JsonResponse(friend.getName() + " is already in your friend list.");

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
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getFriendRequests().stream().filter(k -> k.getId().equals(requestId)).findFirst().orElse(null);
        if(request == null) return new JsonResponse(JsonResponseType.ERROR, "Request doesn't exist.");

        final Player friend = request.getRequester();
        if(friend == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        if(!player.addFriend(friend)) return new JsonResponse(friend.getName() + " is already in your friend list."); // TODO: SysMsg

        friend.addFriend(player);
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
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final FriendRequest request = player.getFriendRequests().stream().filter(k -> k.getId().equals(requestId)).findFirst().orElse(null);
        if(request == null) return new JsonResponse(JsonResponseType.ERROR, "Request doesn't exist.");

        final Player friend = request.getRequester();
        if(friend == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        friend.getFriendRequests().remove(request);
        player.getFriendRequests().remove(request);

        friendRequestService.delete(request);
        playerService.update(friend);
        playerService.update(player);

        return new JsonResponse(friend.getName() + " has been refused !"); // TODO: SysMsg
    }

    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public JsonResponse showAllPlayers() {
        return new JsonResponse(playerService.findAll());
    }
}
