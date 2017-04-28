package com.middlewar.api.controllers.actions;

import com.middlewar.core.model.Account;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.Player;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.model.social.PrivateMessage;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.impl.PrivateMessageServiceImpl;
import com.middlewar.api.util.response.JsonResponse;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.core.utils.TimeUtil;
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
@RequestMapping(value = "/pm", produces = "application/json")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageServiceImpl service;

    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse showAll(@AuthenticationPrincipal Account pAccount) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);
        //service.findBy(new Sort(Sort.Direction.DESC, "date"), Criteria.where("author._id").is(new ObjectId(player.getId())).orOperator(Criteria.where("receiver._id").is(new ObjectId(player.getId()))));
        return new JsonResponse(JsonResponseType.ERROR, "TODO: implement me");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse show(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "id") String pmId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final PrivateMessage pm = service.findOne(pmId);
        if(!pm.getAuthor().getId().equals(player.getId()) && !pm.getReceiver().getId().equals(player.getId()))
            return new JsonResponse(JsonResponseType.ERROR, "Invalid request");

        if(!pm.isRead()) {
            pm.setRead(true);
            pm.setReadDate(TimeUtil.getCurrentTime());
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
