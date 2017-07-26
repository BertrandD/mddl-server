package com.middlewar.controllers.ship;

import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.ShipService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/ship", produces = "application/json")
public class ShipController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ShipService shipService;

    @RequestMapping(method = RequestMethod.POST)
    public Response create(@AuthenticationPrincipal Account pAccount,
                           @RequestParam(value = "count") Long count,
                           @RequestParam(value = "structureId") String structure,
                           @RequestParam(value = "attachments") List<String> ids) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new Response(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);
        base.initializeStats();

        if(ItemData.getInstance().getStructure(structure) == null) return new Response(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        final BaseInventory inventory = base.getBaseInventory();
        final List<ItemInstance> collector = new ArrayList<>();

        final ItemInstance structuresInst = inventory.getItemsToMap().get(structure);
        if(structuresInst == null || structuresInst.getCount() < count) return new Response(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND); // todo: make a new sysmsg

        boolean faillure = false;
        for (int i = 0; i < ids.size() && !faillure; i++){
            final GameItem template = ItemData.getInstance().getTemplate(ids.get(i));
            if(template == null) faillure = true; else {
                final ItemInstance inst = inventory.getItemsToMap().get(template.getItemId());
                if(inst != null && inst.getCount() >= count) collector.add(inst); else faillure = true;
            }
        }

        if(faillure) return new Response(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        for (ItemInstance inst : collector)
            inventoryService.consumeItem(inst, 1);

        final Ship ship = shipService.create(base, structure, count, ids);
        if(ship == null) return new Response(JsonResponseType.ERROR, "Cannot create Ship");
        return new Response(ship);
    }

}
