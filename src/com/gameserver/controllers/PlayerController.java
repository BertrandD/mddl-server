package com.gameserver.controllers;

import com.auth.Account;
import com.config.Config;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.Lang;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.services.InventoryService;
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
@RequestMapping(value = "/player", produces = "application/json")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @JsonView(View.Standard.class)
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JsonResponse players(){
        return new JsonResponse(playerService.findAll());
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse player(@AuthenticationPrincipal Account account, @PathVariable("id") String id){
        final SystemMessageData SM = SystemMessageData.getInstance();
        final Player player = playerService.findOne(id);
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SM.getMessage(account.getLang(), SystemMessageId.PLAYER_NOT_FOUND));
        return new JsonResponse(player);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name){
        final SystemMessageData SM = SystemMessageData.getInstance();
        if(playerService.findOneByName(name) != null) return new JsonResponse(JsonResponseType.ERROR, SM.getMessage(account.getLang(), SystemMessageId.USERNAME_ALREADY_EXIST));

        // Check if name is forbidden (Like 'fuck', 'admin', ...)
        if (Config.FORBIDDEN_NAMES.length > 1)
        {
            for (String st : Config.FORBIDDEN_NAMES)
            {
                if (name.toLowerCase().contains(st.toLowerCase()))
                {
                    return new JsonResponse(JsonResponseType.ERROR, SM.getMessage(account.getLang(), SystemMessageId.FORBIDDEN_NAME));
                }
            }
        }

        final Player player = playerService.create(account, name);
        inventoryService.create(player);
        return new JsonResponse(player);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam("id") String id){
        playerService.delete(id);
    }
}
