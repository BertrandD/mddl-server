package com.middlewar.api.controllers.buildings;

import com.middlewar.api.services.InventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.ValidatorService;
import com.middlewar.api.util.response.JsonResponse;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.ItemFactory;
import com.middlewar.core.model.buildings.ModuleFactory;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Item;
import com.middlewar.core.model.items.Module;
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
@RequestMapping(value = "/factory", produces = "application/json")
public class ItemFactoryController {

    private static final String MODULE_FACTORY = "module_factory";
    private static final String STRUCTURE_FACTORY = "structure_factory";

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validator;

    @RequestMapping(value = "/{factory}/create/{id}", method = RequestMethod.POST)
    public JsonResponse createItem(@AuthenticationPrincipal Account pAccount,
                                   @PathVariable(value = "factory") String factoryType,
                                   @PathVariable(value = "id") String itemId) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        base.initializeStats();

        // TODO : I'm sure we can improve this...
        Item item = null;
        String buildingType = "";
        switch (factoryType) {
            case "module":
                item = ItemData.getInstance().getModule(itemId);
                buildingType = MODULE_FACTORY;
                break;
            case "structure":
                item = ItemData.getInstance().getStructure(itemId);
                buildingType = STRUCTURE_FACTORY;
                break;
        }
        if(item == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        final String finalBuildingType = buildingType;
        final BuildingInstance factory = base.getBuildings().stream().filter(k->k.getBuildingId().equals(finalBuildingType)).findFirst().orElse(null);
        if(factory == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.BUILDING_NOT_FOUND);

        final ItemFactory factoryTemplate = (ItemFactory)factory.getTemplate();
        if(!factoryTemplate.hasItem(factory.getCurrentLevel(), item.getItemId())) return new JsonResponse(JsonResponseType.ERROR, "Module not unlocked !");

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        final JsonResponse faillure = validator.validateItemRequirements(base, item, collector, pAccount.getLang());
        if(faillure != null) return faillure;

        collector.forEach(inventoryService::consumeItem);

        final ItemInstance itemInstance = inventoryService.addItem(base.getBaseInventory(), item.getItemId(), 1);
        if(itemInstance == null) return new JsonResponse(JsonResponseType.ERROR, "Item cannot be created.");

        return new JsonResponse(base);
    }
}
