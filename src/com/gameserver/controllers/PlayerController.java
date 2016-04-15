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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/player", produces = "application/json")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<Player> players(){
        return playerService.findAll();
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Player player(@PathVariable("id") String id){
        return playerService.findOne(id);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account account, @RequestParam(value = "name") String name){
        if(playerService.findOneByName(name) != null) return null;

        // Check if name is forbidden (Like 'fuck', 'admin', ...)
        if (Config.FORBIDDEN_NAMES.length > 1)
        {
            for (String st : Config.FORBIDDEN_NAMES)
            {
                if (name.toLowerCase().contains(st.toLowerCase()))
                {
                    return new JsonResponse(JsonResponseType.ERROR, SystemMessageData.getInstance().getMessage(Lang.EN, SystemMessageId.FORBIDDEN_NAME));
                }
            }
        }

        Player player = playerService.create(account, name);
        inventoryService.create(player);
        return new JsonResponse(player);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam("id") String id){
        playerService.delete(id);
    }
}
