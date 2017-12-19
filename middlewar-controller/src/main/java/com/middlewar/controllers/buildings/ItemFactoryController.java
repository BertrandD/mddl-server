package com.middlewar.controllers.buildings;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.FactoryManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
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

    @RequestMapping(value = "/module/create/{id}", method = RequestMethod.POST)
    public Response createModule(@AuthenticationPrincipal Account pAccount,
                                 @PathVariable(value = "baseId") Long baseId,
                                 @PathVariable(value = "factoryId") Long factoryId,
                                 @PathVariable(value = "id") String itemId) {
        return new Response(factoryManager.createModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), factoryId, itemId));
    }

    @RequestMapping(value = "/structure/create/{id}", method = RequestMethod.POST)
    public Response createStructure(@AuthenticationPrincipal Account pAccount,
                                    @PathVariable(value = "baseId") Long baseId,
                                    @PathVariable(value = "factoryId") Long factoryId,
                                    @PathVariable(value = "id") String itemId) {
        return new Response(factoryManager.createStructure(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), factoryId, itemId));
    }
}
