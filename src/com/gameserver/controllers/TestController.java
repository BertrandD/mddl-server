package com.gameserver.controllers;

import com.auth.Account;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.model.items.GameItem;
import com.gameserver.services.InventoryService;
import com.gameserver.services.PlayerService;
import com.gameserver.services.UpdateService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
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
@PreAuthorize("hasRole('ROLE_USER')") // TODO: ROLE_ADMIN
@RequestMapping(value = "/admin", produces = "application/json")
public class TestController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UpdateService updateService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonResponse test() {
        return new JsonResponse();
    }

    @RequestMapping(value = "/mongodb/update", method = RequestMethod.GET)
    public JsonResponse updateDatabase() {
        updateService.updateDatabase();
        return new JsonResponse("Static message : Database updated successfully !");
    }

    @RequestMapping(value = "/create/item", method = RequestMethod.POST)
    public JsonResponse createItem(@AuthenticationPrincipal Account pAccount, @RequestParam(name = "itemId") String itemId, @RequestParam(name = "count") long count) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.BASE_NOT_FOUND);

        GameItem item = ItemData.getInstance().getTemplate(itemId);
        if(item == null) return new JsonResponse(JsonResponseType.ERROR, "Item ["+itemId+"] doesnt exist !");

        if(item.getType().equals(ItemType.RESOURCE)) inventoryService.addItem(base.getResourcesInventory(), itemId, count);
        else inventoryService.addItem(base.getBaseInventory(), itemId, count);

        return new JsonResponse("Item added successful !");
    }
}
