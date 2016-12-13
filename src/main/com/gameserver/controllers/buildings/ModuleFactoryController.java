package com.gameserver.controllers.buildings;

import com.middlewar.core.model.Account;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.ModuleFactory;
import com.util.response.SystemMessageId;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Module;
import com.gameserver.services.InventoryService;
import com.gameserver.services.PlayerService;
import com.gameserver.services.ValidatorService;
import com.util.response.JsonResponse;
import com.util.response.JsonResponseType;
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
@RequestMapping(value = "/modulefactory", produces = "application/json")
public class ModuleFactoryController {

    private static final String MODULE_FACTORY = "module_factory";

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validator;

    @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
    public JsonResponse createModule(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "id") String moduleId) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        base.initializeStats();

        final Module module = ItemData.getInstance().getModule(moduleId);
        if(module == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        final BuildingInstance factory = base.getBuildings().stream().filter(k->k.getBuildingId().equals(MODULE_FACTORY)).findFirst().orElse(null);
        if(factory == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.BUILDING_NOT_FOUND);

        final ModuleFactory factoryTemplate = (ModuleFactory)factory.getTemplate();
        if(!factoryTemplate.hasModule(factory.getCurrentLevel(), module.getItemId())) return new JsonResponse(JsonResponseType.ERROR, "Module not unlocked !");

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        final JsonResponse faillure = validator.validateItemRequirements(base, module, collector, pAccount.getLang());
        if(faillure != null) return faillure;

        collector.forEach(inventoryService::consumeItem);

        final ItemInstance item = inventoryService.addItem(base.getBaseInventory(), module.getItemId(), 1);
        if(item == null) return new JsonResponse(JsonResponseType.ERROR, "Module cannot be created.");

        return new JsonResponse(base);
    }
}
