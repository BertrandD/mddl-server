package com.middlewar.controllers.buildings;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.FactoryManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import com.middlewar.dto.inventory.ItemInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/me/base/{baseId}/factory/{factoryId}", produces = "application/json")
public class ItemFactoryController {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private FactoryManager factoryManager;

    @Autowired
    private ControllerManagerWrapper controllerManagerWrapper;

    @RequestMapping(value = Route.ITEM_FACTORY_CREATE_MODULE, method = RequestMethod.POST)
    public ItemInstanceDto createModule(@AuthenticationPrincipal Account pAccount,
                                        @PathVariable(value = "baseId") int baseId,
                                        @PathVariable(value = "factoryId") int factoryId,
                                        @PathVariable(value = "id") String itemId) {
        return factoryManager.createModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), factoryId, itemId).toDTO();
    }

    @RequestMapping(value = Route.ITEM_FACTORY_CREATE_STRUCTURE, method = RequestMethod.POST)
    public ItemInstanceDto createStructure(@AuthenticationPrincipal Account pAccount,
                                           @PathVariable(value = "baseId") int baseId,
                                           @PathVariable(value = "factoryId") int factoryId,
                                           @PathVariable(value = "id") String itemId) {
        return factoryManager.createStructure(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), factoryId, itemId).toDTO();
    }
}
