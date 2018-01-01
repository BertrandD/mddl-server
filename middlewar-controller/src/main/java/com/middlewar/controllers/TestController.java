package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.services.impl.PlayerServiceImpl;
import com.middlewar.api.services.impl.InventoryServiceImpl;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.items.GameItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/admin", produces = "application/json")
public class TestController {

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Response test() {
        return new Response();
    }

    @RequestMapping(value = "/create/item", method = RequestMethod.POST)
    public Response createItem(@AuthenticationPrincipal Account pAccount, @RequestParam(name = "itemId") String itemId, @RequestParam(name = "count") Long count) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if (base == null) return new Response(SystemMessageId.BASE_NOT_FOUND);

        GameItem item = ItemData.getInstance().getTemplate(itemId);
        if (item == null) return new Response("Item [" + itemId + "] doesnt exist !");

        base.initializeStats();
        inventoryService.addItem(base.getBaseInventory(), itemId, count);

        return new Response("Item added successful !");
    }
}
