package com.gameserver.controllers;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.items.GameItem;
import com.gameserver.services.InventoryService;
import com.gameserver.services.PlayerInventoryService;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private InventoryService inventoryService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/base/item", method = RequestMethod.POST)
    public JsonResponse createItemInBaseInventory(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "itemId") String itemId, @RequestParam(value = "count") long count) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        final GameItem tmpl = ItemData.getInstance().getTemplate(itemId);
        if(tmpl == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.ITEM_NOT_FOUND);

        final Inventory inventory;
        switch(tmpl.getType())
        {
            case RESOURCE:
                inventory = base.getResourcesInventory();
                break;
            case CARGO:
            case ENGINE:
            case MODULE:
            case WEAPON:
            case STRUCTURE:
                inventory = base.getBaseInventory();
                break;
            default:
                inventory = base.getBaseInventory();
                break;
        }

        final ItemInstance item = inventoryService.addItem(inventory, itemId, count);
        if(item == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.ITEM_CANNOT_CREATE);

        return new JsonResponse(item);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/player/item", method = RequestMethod.POST)
    public JsonResponse createItemInPlayerInventory(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "itemId") String itemId, @RequestParam(value = "count") long count) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);

        final PlayerInventory inventory = playerInventoryService.findByPlayer(player.getId());
        if(inventory == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.INVENTORY_NOT_FOUND); // TODO JsonResponse

        final ItemInstance item = inventoryService.addItem(inventory, itemId, count);
        if(item == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.ITEM_CANNOT_CREATE);

        return new JsonResponse(item);
    }
}
