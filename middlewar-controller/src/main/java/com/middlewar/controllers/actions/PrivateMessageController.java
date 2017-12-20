package com.middlewar.controllers.actions;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.PrivateMessageService;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import com.middlewar.core.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/pm", produces = "application/json")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService service;

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = Route.PM_ALL, method = RequestMethod.GET)
    public Response showAll(@AuthenticationPrincipal Account pAccount) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);
        //service.findByBaseAndId(new Sort(Sort.Direction.DESC, "date"), Criteria.where("author._id").is(new ObjectId(player.getId())).orOperator(Criteria.where("receiver._id").is(new ObjectId(player.getId()))));
        return new Response("TODO: implement me");
    }

    @RequestMapping(value = Route.PM_ONE, method = RequestMethod.GET)
    public Response show(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "id") int pmId) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        final PrivateMessage pm = service.findOne(pmId);
        if (pm.getAuthor().getId() != (player.getId()) && pm.getReceiver().getId() != (player.getId()))
            return new Response("Invalid request");

        if (!pm.isRead()) {
            pm.setRead(true);
            pm.setReadDate(TimeUtil.getCurrentTime());
        }

        return new Response(pm);
    }

    @RequestMapping(value = Route.PM_SEND, method = RequestMethod.POST)
    public Response send(@AuthenticationPrincipal Account pAccount, @RequestParam("receiver") int receiverId, @RequestParam("message") String message) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        final Player receiver = playerService.findOne(receiverId);
        if (receiver == null) return new Response("Invalid receiver. Player not found");

        // TODO: Check sender != receiver

        // TODO: Convert tag likes [b], [u], [url], [emote], ...
        // TODO: Convert tag likes [Base:462323846], [Player:Shadow38], [Planet:4658545], ...
        // TODO: Check forbidden words (hating, ad, etc...)

        final PrivateMessage pm = service.create(player, receiver, message);
        if (pm == null)
            return new Response("An error occurred. We can't send your private message.");

        return new Response(pm);
    }
}
