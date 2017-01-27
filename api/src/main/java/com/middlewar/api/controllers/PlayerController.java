package com.middlewar.api.controllers;

import com.middlewar.core.model.Account;
import com.middlewar.api.auth.AccountService;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.Player;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.JsonResponse;
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

    @RequestMapping(value = "/me/player", method = RequestMethod.GET)
    public JsonResponse players(@AuthenticationPrincipal Account pAccount){
        //final Account account = accountService.findOne(pAccount.getId());
        return new JsonResponse(playerService.findBy(pAccount));
    }

    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public JsonResponse showAllPlayers() {
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
        if(account.getPlayers().size() >= Config.MAX_PLAYER_IN_ACCOUNT) return new JsonResponse("Maximum player creation reached !"); // TODO: SysMsg

        if(playerService.findByName(name) != null) return new JsonResponse(account.getLang(), SystemMessageId.USERNAME_ALREADY_EXIST);

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
}
