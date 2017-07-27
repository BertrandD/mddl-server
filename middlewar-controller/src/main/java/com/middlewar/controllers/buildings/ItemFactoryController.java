package com.middlewar.controllers.buildings;

import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.FactoryManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
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

    @RequestMapping(value = "/me/base/{baseId}/factory/{factoryId}/module/create/{id}", method = RequestMethod.POST)
    public Response createModule(@AuthenticationPrincipal Account pAccount,
                                 @PathVariable(value = "baseId") Long baseId,
                                 @PathVariable(value = "factoryId") Long factoryId,
                                 @PathVariable(value = "id") String itemId) {
        return controllerManagerWrapper.wrap(() -> factoryManager.createModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), factoryId, itemId));
    }

    @RequestMapping(value = "/me/base/{baseId}/factory/{factoryId}/structure/create/{id}", method = RequestMethod.POST)
    public Response createStructure(@AuthenticationPrincipal Account pAccount,
                                    @PathVariable(value = "baseId") Long baseId,
                                    @PathVariable(value = "factoryId") Long factoryId,
                                    @PathVariable(value = "id") String itemId) {
        return controllerManagerWrapper.wrap(() -> factoryManager.createStructure(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), factoryId, itemId));
    }
}
