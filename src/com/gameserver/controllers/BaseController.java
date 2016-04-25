package com.gameserver.controllers;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingTaskService;
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
public class BaseController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BuildingTaskService buildingTaskService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/me/base", method = RequestMethod.GET)
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount){
        if(pAccount.getCurrentPlayer() == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        return new JsonResponse(player.getBases());
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/me/base/{id}", method = RequestMethod.GET)
    public JsonResponse findOne(@AuthenticationPrincipal Account account, @PathVariable("id") String id){
        // TODO: check base owner
        final Base base = baseService.findOne(id);
        if(base == null) return new JsonResponse(account.getLang(), SystemMessageId.BASE_NOT_FOUND);

        // Update current player base
        base.getOwner().setCurrentBase(base);
        playerService.update(base.getOwner());

        final JsonResponse response = new JsonResponse(base);
        response.addMeta("queue", buildingTaskService.findByBaseOrderByEndsAtAsc(base.getId()));
        return response;
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/base", method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "name") String name) {
        Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        final Base base = baseService.create(name, player);
        player.addBase(base);
        player.setCurrentBase(base);
        playerService.update(player);
        return new JsonResponse(base);
    }
}
