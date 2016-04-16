package com.gameserver.controllers;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.services.BaseService;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
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
@RequestMapping(value = "/base", produces = "application/json")
public class BaseController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerService playerService;

    @JsonView(View.Standard.class)
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JsonResponse findAll(){
        return new JsonResponse(baseService.findAll());
    }

    @JsonView({View.Standard.class})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findOne(@AuthenticationPrincipal Account account, @PathVariable("id") String id){
        final Base base = baseService.findOne(id);
        if(base == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageData.getInstance().getMessage(account.getLang(), SystemMessageId.BASE_NOT_FOUND));
        return new JsonResponse(base);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name, @RequestParam(value = "player") String playerId) {
        Player player = playerService.findOne(playerId);
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageData.getInstance().getMessage(account.getLang(), SystemMessageId.PLAYER_NOT_FOUND));
        Base base = baseService.create(name, player);
        player.addBase(base);
        playerService.update(player);
        return new JsonResponse(base);
    }
}
