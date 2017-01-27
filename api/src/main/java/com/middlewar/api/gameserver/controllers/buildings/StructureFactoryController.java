package com.middlewar.api.gameserver.controllers.buildings;

import com.middlewar.api.gameserver.services.InventoryService;
import com.middlewar.api.gameserver.services.PlayerService;
import com.middlewar.api.gameserver.services.ValidatorService;
import com.middlewar.api.util.response.JsonResponse;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.StructureFactory;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Structure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/structurefactory", produces = "application/json")
public class StructureFactoryController {

    private static final String STRUCTURE_FACTORY = "structure_factory";

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validator;

    @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
    public JsonResponse createStructure(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "id") String structureId) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        base.initializeStats();

        final Structure structure = ItemData.getInstance().getStructure(structureId);
        if(structure == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        final BuildingInstance factory = base.getBuildings().stream().filter(k->k.getBuildingId().equals(STRUCTURE_FACTORY)).findFirst().orElse(null);
        if(factory == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.BUILDING_NOT_FOUND);

        final StructureFactory factoryTemplate = (StructureFactory) factory.getTemplate();
        if(!factoryTemplate.hasItem(factory.getCurrentLevel(), structure.getItemId())) return new JsonResponse(JsonResponseType.ERROR, "Structure not unlocked !");

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        final JsonResponse faillure = validator.validateItemRequirements(base, structure, collector, pAccount.getLang());
        if(faillure != null) return faillure;

        collector.forEach(inventoryService::consumeItem);

        final ItemInstance item = inventoryService.addItem(base.getBaseInventory(), structure.getItemId(), 1);
        if(item == null) return new JsonResponse(JsonResponseType.ERROR, "Structure cannot be created.");

        return new JsonResponse(base);
    }
}
