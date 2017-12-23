package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import com.middlewar.dto.PlayerDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "players", produces = "application/json")
@RequestMapping(consumes = "application/json", produces = "application/json")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerManager playerManager;

    @RequestMapping(value = Route.PLAYER_ALL_OWNED, method = RequestMethod.GET)
    public Response players(@AuthenticationPrincipal Account pAccount) {
        return new Response(playerManager.getAllPlayersForAccount(pAccount));
    }

    @ApiOperation(value = "Return all players", notes = "This method must be turned off and used as ROLE_ADMIN")
    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public Response showAllPlayers() {
        // TODO: used for tests. Remove when administration will be done
        return new Response(playerService.findAll());
    }
*/

    @RequestMapping(value = Route.PLAYER_ONE, method = RequestMethod.GET)
    public PlayerDto player(@AuthenticationPrincipal Account account, @PathVariable("id") Long id) {
        return playerManager.getPlayerOfAccount(account, id).toDTO();
    }

    @RequestMapping(value = Route.PLAYER_CREATE, method = RequestMethod.POST)
    public PlayerDto create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name) {
        return playerManager.createForAccount(account, name).toDTO();

    }
}
