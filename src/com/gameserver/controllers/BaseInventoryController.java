package com.gameserver.controllers;

import com.auth.Account;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/base/inventory", produces = "application/json")
public class BaseInventoryController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse getBaseInventories(@AuthenticationPrincipal Account pAccount){
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        final HashMap<String, BaseInventory> inventories = new HashMap<>();
        inventories.put("RESOURCE", base.getResources());
        inventories.put("SHIP_ITEM", base.getShipItems());
        inventories.put("COMMON", base.getCommons());
        return new JsonResponse(inventories);
    }
}
