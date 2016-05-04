package com.gameserver.controllers;

import com.auth.Account;
import com.auth.AccountService;
import com.config.Config;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.View;
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

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AccountService accountService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/me/player", method = RequestMethod.GET)
    public JsonResponse players(@AuthenticationPrincipal Account pAccount){
        final Account account = accountService.findOne(pAccount.getId());
        return new JsonResponse(playerService.findByAccount(account));
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/me/player/{id}", method = RequestMethod.GET)
    public JsonResponse player(@AuthenticationPrincipal Account account, @PathVariable("id") String id){
        final Player player = playerService.findOne(id);
        if(player == null) return new JsonResponse(account.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        return new JsonResponse(player);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/player", method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name){
        if(playerService.findOneByName(name) != null) return new JsonResponse(account.getLang(), SystemMessageId.USERNAME_ALREADY_EXIST);

        // Check if name is forbidden (Like 'fuck', 'admin', ...)
        if (Config.FORBIDDEN_NAMES.length > 1)
        {
            for (String st : Config.FORBIDDEN_NAMES)
            {
                if (name.toLowerCase().contains(st.toLowerCase()))
                {
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

        accountService.update(playerAccount);
        return new JsonResponse(player);
    }
}
