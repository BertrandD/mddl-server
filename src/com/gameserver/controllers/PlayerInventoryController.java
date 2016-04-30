package com.gameserver.controllers;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.services.PlayerInventoryService;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/player/inventory", produces = "application/json")
public class PlayerInventoryController{

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private PlayerService playerService;

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse getPlayerInventory(@AuthenticationPrincipal Account pAccount){
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);

        final PlayerInventory inventory = playerInventoryService.findByPlayer(player.getId());
        return new JsonResponse(inventory);
    }
}
