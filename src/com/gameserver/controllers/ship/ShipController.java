package com.gameserver.controllers.ship;

import com.auth.Account;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.vehicles.Ship;
import com.gameserver.services.InventoryService;
import com.gameserver.services.PlayerService;
import com.gameserver.services.ShipService;
import com.util.response.JsonResponse;
import com.util.response.JsonResponseType;
import com.util.response.SystemMessageId;
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
    public JsonResponse create(@AuthenticationPrincipal Account pAccount,
                               @RequestParam(value = "count") long count,
                               @RequestParam(value = "structureId") String structure,
                               @RequestParam(value = "attachments") List<String> ids) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);
        base.initializeStats();

        if(ItemData.getInstance().getStructure(structure) == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        final BaseInventory inventory = base.getBaseInventory();
        final List<ItemInstance> collector = new ArrayList<>();

        final ItemInstance structuresInst = inventory.getItems().stream().filter(i->i.getTemplateId().equals(structure)).findFirst().orElse(null);
        if(structuresInst == null || structuresInst.getCount() < count) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND); // todo: make a new sysmsg

        boolean faillure = false;
        for (int i = 0; i < ids.size() && !faillure; i++){
            final GameItem template = ItemData.getInstance().getTemplate(ids.get(i));
            if(template == null) faillure = true; else {
                final ItemInstance inst = inventory.getItems().stream().filter(item -> item.getTemplateId().equals(template.getItemId())).findFirst().orElse(null);
                if(inst != null && inst.getCount() >= count) collector.add(inst); else faillure = true;
            }
        }

        if(faillure) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        for (ItemInstance inst : collector)
            inventoryService.consumeItem(inst, 1);

        final Ship ship = shipService.create(base, structure, count, ids);
        if(ship == null) return new JsonResponse(JsonResponseType.ERROR, "Cannot create Ship");
        return new JsonResponse(ship);
    }

}
