package com.middlewar.controllers.instances;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.dto.instances.BuildingInstanceDto;
import com.middlewar.request.BuildingCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private BuildingManager buildingManager;

    @RequestMapping(method = RequestMethod.GET)
    public Response getBuilding(@AuthenticationPrincipal Account account, @RequestBody BuildingCreationRequest request) {
        return new Response(buildingManager.create(account.getCurrentPlayer(), request.getBaseId(), request.getTemplateId()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public BuildingInstanceDto create(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") int baseId, @RequestParam(value = "building") String templateId) {
        return buildingManager.create(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), templateId).toDTO();
    }

    @RequestMapping(value = Route.BUILDING_UPGRADE, method = RequestMethod.PUT)
    public BuildingInstanceDto upgrade(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") int baseId, @PathVariable("id") int id) {
        return buildingManager.upgrade(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), id).toDTO();
    }

    @RequestMapping(value = Route.BUILDING_ATTACH_MODULE, method = RequestMethod.PUT)
    public Response attachModule(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") int baseId, @PathVariable("id") int buildingInstId, @PathVariable("module") String moduleId) {
        return new Response(buildingManager.attachModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), buildingInstId, moduleId));
    }
}
