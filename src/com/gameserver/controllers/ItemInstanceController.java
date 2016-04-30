package com.gameserver.controllers;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.services.ItemService;
import com.gameserver.services.PlayerInventoryService;
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
public class ItemInstanceController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ItemInstance findOne(@PathVariable(value = "id") String id){
        final ItemInstance item = itemService.findOne(id);
        if(item == null) return null; // TODO: JsonResponse


        return item;
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/base/item", method = RequestMethod.POST)
    public JsonResponse createItemInBaseInventory(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "itemId") String itemId, @RequestParam(value = "count") long count) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new JsonResponse("player is null"); // TODO: JsonResponse

        Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse("Current base is null"); // TODO JsonResponse

        final ItemInstance item = itemService.create(base, itemId, count);
        if(item == null) return null; // TODO: JsonResponse
        return new JsonResponse(item);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/player/item", method = RequestMethod.POST)
    public JsonResponse createItemInPlayerInventory(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "itemId") String itemId, @RequestParam(value = "count") long count) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new JsonResponse("player is null"); // TODO: JsonResponse

        final PlayerInventory inventory = playerInventoryService.findByPlayer(player.getId());
        if(inventory == null) return new JsonResponse("player inventory is null"); // TODO JsonResponse

        final ItemInstance item = itemService.create(inventory, itemId, count);
        if(item == null) return new JsonResponse("item instance is null"); // TODO: JsonResponse

        inventory.addItem(item);
        playerInventoryService.update(inventory);

        return new JsonResponse(item);
    }
}
