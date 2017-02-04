package com.middlewar.api.controllers;

import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.core.model.Account;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Player;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.JsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
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

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/me/player", method = RequestMethod.GET)
    public JsonResponse players(@AuthenticationPrincipal Account pAccount){
        return new JsonResponse(playerService.findBy(pAccount));
    }

    @ApiOperation(value = "Return all players", notes = "This method must be turned off and used as ROLE_ADMIN", response = JsonResponse.class)
    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public JsonResponse showAllPlayers() {
        // TODO: used for tests. Remove when administration will be done
        return new JsonResponse(playerService.findAll());
    }

    @RequestMapping(value = "/me/player/{id}", method = RequestMethod.GET)
    public JsonResponse player(@AuthenticationPrincipal Account account, @PathVariable("id") String id){
        final Player player = playerService.findOne(id);
        if(player == null) return new JsonResponse(account.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        return new JsonResponse(player);
    }

    @RequestMapping(value = "/player", method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name) {
        Assert.notNull(name, SystemMessageId.INVALID_PARAMETERS);

        if(account.getPlayers().size() >= Config.MAX_PLAYER_IN_ACCOUNT)
            return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.MAXIMUM_PLAYER_CREATION_REACHED);

        if(playerService.findByName(name) != null) return new JsonResponse(account.getLang(), SystemMessageId.USERNAME_ALREADY_EXIST);

        // Check if name is forbidden (Like 'fuck', 'admin', ...)
        if (Config.FORBIDDEN_NAMES.length > 1) {
            for (String st : Config.FORBIDDEN_NAMES) {
                if (name.toLowerCase().contains(st.toLowerCase())) {
                    logger.info("Player creation failed for account [ " + account.getUsername() + " ] : Forbidden name.");
                    return new JsonResponse(account.getLang(), SystemMessageId.FORBIDDEN_NAME);
                }
            }
        }

        // Create player
        final Player player = playerService.create(account, name);
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_CREATION_FAILED);

        logger.info("Player creation success : "+ player.getName() +".");

        return new JsonResponse(player);
    }
}
