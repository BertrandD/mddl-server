package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.exception.PlayerNotFoundException;
import com.middlewar.core.model.Account;
import com.middlewar.request.PlayerCreationRequest;
import com.middlewar.request.PlayerUpdateRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.middlewar.core.predicate.PlayerPredicate.hasId;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@Api(value = "players", consumes = "application/json", produces = "application/json")
@RequestMapping(name = "/player", consumes = "application/json", produces = "application/json")
public class PlayerController {

    @Autowired
    private PlayerManager playerManager;

    @RequestMapping(method = GET)
    public Response players(@AuthenticationPrincipal Account account) {
        return new Response(account.getPlayers());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Response player(@AuthenticationPrincipal Account account, @PathVariable("id") int id) {
        return new Response(account.getPlayers().stream().filter(hasId(id)).findFirst().orElseThrow(PlayerNotFoundException::new));
    }

    @RequestMapping(method = POST)
    public Response create(@AuthenticationPrincipal Account account, @RequestBody PlayerCreationRequest request) {
        return new Response(playerManager.create(account, request.getName()));
    }

    @RequestMapping(method = PUT)
    public Response update(@AuthenticationPrincipal Account account, @RequestBody PlayerUpdateRequest request) {
        //TODO
        return new Response();
    }
}
