package com.middlewar.controllers;

import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.PlayerNotOwnedException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import com.middlewar.dto.PlayerDTO;
import io.swagger.annotations.Api;
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
@Api(value = "players", produces = "application/json")
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class PlayerController {

    private final PlayerService playerService;

    private final PlayerManager playerManager;

    private final ControllerManagerWrapper controllerManagerWrapper;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerManager playerManager, ControllerManagerWrapper controllerManagerWrapper) {
        this.playerService = playerService;
        this.playerManager = playerManager;
        this.controllerManagerWrapper = controllerManagerWrapper;
    }

    @RequestMapping(value = Route.PLAYER_ALL_OWNED, method = RequestMethod.GET)
    public Response players(@AuthenticationPrincipal Account pAccount) {
        return controllerManagerWrapper.wrap(() -> playerManager.getAllPlayersForAccount(pAccount));
    }

/*
    @ApiOperation(value = "Return all players", notes = "This method must be turned off and used as ROLE_ADMIN", response = Response.class)
    @RequestMapping(value = Route.PLAYER_ALL, method = RequestMethod.GET)
    public Response showAllPlayers() {
        // TODO: used for tests. Remove when administration will be done
        return new Response<>(playerService.findAll());
    }
*/

    @RequestMapping(value = Route.PLAYER_ONE, method = RequestMethod.GET)
    public PlayerDTO player(@AuthenticationPrincipal Account account, @PathVariable("id") Long id) {
        return playerManager.getPlayerOfAccount(account, id).toDTO();
    }

    @RequestMapping(value = Route.PLAYER_CREATE, method = RequestMethod.POST)
    public PlayerDTO create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name) {
        return playerManager.createForAccount(account, name).toDTO();

    }
}
