package com.middlewar.api.controllers.buildings;

import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.FactoryManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.ValidatorService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.ItemFactory;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Item;
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
@RequestMapping(value = "", produces = "application/json")
public class ItemFactoryController {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private FactoryManager factoryManager;

    @Autowired
    private ControllerManagerWrapper controllerManagerWrapper;

    @RequestMapping(value = "/me/base/{baseId}/factory/module/create/{id}", method = RequestMethod.POST)
    public Response createModule(@AuthenticationPrincipal Account pAccount,
                                 @PathVariable(value = "baseId") String baseId,
                                 @PathVariable(value = "id") String itemId) {
        return controllerManagerWrapper.wrap(() -> factoryManager.createModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), itemId));
    }

    @RequestMapping(value = "/me/base/{baseId}/factory/structure/create/{id}", method = RequestMethod.POST)
    public Response createStructure(@AuthenticationPrincipal Account pAccount,
                                 @PathVariable(value = "baseId") String baseId,
                                 @PathVariable(value = "id") String itemId) {
        return controllerManagerWrapper.wrap(() -> factoryManager.createStructure(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), itemId));
    }
}
