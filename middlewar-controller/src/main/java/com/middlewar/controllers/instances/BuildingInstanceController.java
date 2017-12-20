package com.middlewar.controllers.instances;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/me/base/{baseId}/building", produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private BuildingManager buildingManager;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response getBuilding(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") Long baseId, @PathVariable("id") Long id) {
        return new Response(buildingManager.getBuilding(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Response create(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") Long baseId, @RequestParam(value = "building") String templateId) {
        return new Response(buildingManager.create(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), templateId));
    }

    @RequestMapping(value = "/{id}/upgrade", method = RequestMethod.PUT)
    public Response upgrade(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") Long baseId, @PathVariable("id") Long id) {
        return new Response(buildingManager.upgrade(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), id));
    }

    @RequestMapping(value = "/{id}/attach/module/{module}", method = RequestMethod.PUT)
    public Response attachModule(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") Long baseId, @PathVariable("id") Long buildingInstId, @PathVariable("module") String moduleId) {
        return new Response(buildingManager.attachModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), buildingInstId, moduleId));
    }
}
