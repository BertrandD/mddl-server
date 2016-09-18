package com.gameserver.controllers.actions;

import com.auth.Account;
import com.gameserver.holders.PlayerHolder;
import com.gameserver.model.Player;
import com.util.response.SystemMessageId;
import com.gameserver.model.social.PrivateMessage;
import com.gameserver.services.PlayerService;
import com.gameserver.services.PrivateMessageService;
import com.util.response.JsonResponse;
import com.util.response.JsonResponseType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
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
@RequestMapping(value = "/pm", produces = "application/json")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService service;

    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse showAll(@AuthenticationPrincipal Account pAccount) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);
        return new JsonResponse(service.findBy(new Sort(Sort.Direction.DESC, "date"), Criteria.where("author._id").is(new ObjectId(player.getId())).orOperator(Criteria.where("receiver._id").is(new ObjectId(player.getId())))));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse show(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "id") String pmId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final PrivateMessage pm = service.findOne(pmId);
        if(!pm.getAuthor().getId().equals(player.getId()) && !pm.getReceiver().getId().equals(player.getId()))
            return new JsonResponse(JsonResponseType.ERROR, "Invalid request");

        if(!pm.isRead()) {
            pm.setIsRead(true);
            pm.setReadDate(System.currentTimeMillis());
        }

        return new JsonResponse(pm);
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse send(@AuthenticationPrincipal Account pAccount, @RequestParam("receiver") String receiverId, @RequestParam("message") String message) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final Player receiver = playerService.findOne(receiverId);
        if(receiver == null) return new JsonResponse(JsonResponseType.ERROR, "Invalid receiver. Player not found");

        // TODO: Check sender != receiver

        // TODO: Convert tag likes [b], [u], [url], [emote], ...
        // TODO: Convert tag likes [Base:462323846], [Player:Shadow38], [Planet:4658545], ...
        // TODO: Check forbidden words (hating, ad, etc...)

        final PrivateMessage pm = service.create(new PlayerHolder(player), new PlayerHolder(receiver), message);
        if(pm == null) return new JsonResponse(JsonResponseType.ERROR, "An error occurred. We can't send your private message.");

        return new JsonResponse(pm);
    }
}
