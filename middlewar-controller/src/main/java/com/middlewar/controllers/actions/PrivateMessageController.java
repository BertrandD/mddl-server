package com.middlewar.controllers.actions;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.PrivateMessageService;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import com.middlewar.core.utils.TimeUtil;
import com.middlewar.request.PrivateMessageCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

    @RequestMapping(method = GET)
    public Response findAll(@AuthenticationPrincipal Account account) {
        final Player player = account.getCurrentPlayer();
        //service.findByBaseAndId(new Sort(Sort.Direction.DESC, "date"), Criteria.where("author._id").is(new ObjectId(player.getId())).orOperator(Criteria.where("receiver._id").is(new ObjectId(player.getId()))));
        return new Response("TODO: implement me");
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Response find(@AuthenticationPrincipal Account account, @PathVariable(value = "id") int pmId) {
        final Player player = account.getCurrentPlayer();
        final PrivateMessage pm = service.find(pmId);

        if(pm == null) throw new RuntimeException();

        final Player author = pm.getAuthor();
        final Player recipient = pm.getReceiver();

        if (author.getId() != player.getId() && pm.getReceiver().getId() != player.getId()) return new Response("Invalid request");

        if (!pm.isRead() && player.equals(recipient)) {
            pm.setRead(true);
            pm.setReadDate(TimeUtil.getCurrentTime());
            service.update(pm);
        }

        return new Response(pm);
    }

    @RequestMapping(method = POST)
    public Response send(@AuthenticationPrincipal Account account, @RequestBody PrivateMessageCreationRequest request) {
        final Player player = account.getCurrentPlayer();
        final Player receiver = playerService.find(request.getReceiverId());

        if (receiver == null) throw new RuntimeException();

        if(player.equals(receiver)) throw new RuntimeException();

        // TODO: Convert tag likes [b], [u], [url], [emote], ...
        // TODO: Convert tag likes [Base:462323846], [Player:Shadow38], [Planet:4658545], ...
        // TODO: Check forbidden words (hating, ad, etc...)

        final PrivateMessage pm = service.create(player, receiver, request.getMessage());
        if (pm == null)
            return new Response("An error occurred. We can't send your private message.");

        return new Response(pm);
    }
}
