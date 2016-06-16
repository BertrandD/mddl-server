package com.gameserver.controllers.buildings;

import com.auth.Account;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.buildings.ModuleFactory;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.items.Module;
import com.gameserver.services.InventoryService;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/building/modulefactory/interface", produces = "application/json")
public class ModuleFactoryController {

    private static final String MODULE_FACTORY = "module_factory";

    @Autowired
    private PlayerService playerService;

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value = "/create/module/{id}", method = RequestMethod.POST)
    public JsonResponse createModule(@AuthenticationPrincipal Account pAccount, @PathVariable(value = "id") String moduleId) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        final Module module = ItemData.getInstance().getModule(moduleId);
        if(module == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        final BuildingInstance factory = base.getBuildings().stream().filter(k->k.getBuildingId().equals(MODULE_FACTORY)).findFirst().orElse(null);
        if(factory == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.BUILDING_NOT_FOUND);

        final ModuleFactory factoryTemplate = (ModuleFactory)factory.getTemplate();
        if(!factoryTemplate.hasModule(factory.getCurrentLevel(), module.getItemId())) return new JsonResponse(JsonResponseType.ERROR, "Module not unlocked !");

        // TODO: consume requirements (wait for a unique logic for all needed consume requirements)

        final ItemInstance item = inventoryService.addItem(base.getBaseInventory(), module.getItemId(), 1);
        if(item == null) return new JsonResponse(JsonResponseType.ERROR, "Module cannot be created.");

        return new JsonResponse(item);
    }
}