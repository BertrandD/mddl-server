package com.middlewar.controllers;

import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.dto.PlayerDTO;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @RequestMapping(value = "/me/player", method = RequestMethod.GET)
    public Response players(@AuthenticationPrincipal Account pAccount) {
        return controllerManagerWrapper.wrap(() -> playerManager.getAllPlayersForAccount(pAccount));
    }

    @ApiOperation(value = "Return all players", notes = "This method must be turned off and used as ROLE_ADMIN", response = Response.class)
    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public Response showAllPlayers() {
        // TODO: used for tests. Remove when administration will be done
        return new Response<>(playerService.findAll());
    }

    @RequestMapping(value = "/me/player/{id}", method = RequestMethod.GET)
    public Response player(@AuthenticationPrincipal Account account, @PathVariable("id") Long id) {
        return controllerManagerWrapper.wrap(() -> playerManager.getPlayerOfAccount(account, id));
    }

    @RequestMapping(value = "/player", method = RequestMethod.POST)
    public PlayerDTO create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name) throws MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException {
        return new PlayerDTO(playerManager.createForAccount(account, name));

    }
}
