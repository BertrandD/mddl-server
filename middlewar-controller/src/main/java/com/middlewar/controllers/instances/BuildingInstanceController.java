package com.middlewar.controllers.instances;

import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.BuildingAlreadyExistsException;
import com.middlewar.api.exceptions.BuildingCreationException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.BuildingTemplateNotFoundException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import com.middlewar.dto.instances.BuildingInstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private ControllerManagerWrapper controllerManagerWrapper;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private BuildingManager buildingManager;

    @RequestMapping(value = Route.BUILDING_ONE, method = RequestMethod.GET)
    public Response getBuilding(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") int baseId, @PathVariable("id") int id) {
        return controllerManagerWrapper.wrap(() -> buildingManager.getBuilding(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), id));
    }

    @RequestMapping(value = Route.BUILDING_CREATE, method = RequestMethod.POST)
    public BuildingInstanceDTO create(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") int baseId, @RequestParam(value = "building") String templateId) throws NoPlayerConnectedException, PlayerNotFoundException, BaseNotFoundException, BaseNotOwnedException, BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException {
        return buildingManager.create(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), templateId).toDTO();
    }

    @RequestMapping(value = Route.BUILDING_UPGRADE, method = RequestMethod.PUT)
    public Response upgrade(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") int baseId, @PathVariable("id") int id) {
        return controllerManagerWrapper.wrap(() -> buildingManager.upgrade(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), id));
    }

    @RequestMapping(value = Route.BUILDING_ATTACH_MODULE, method = RequestMethod.PUT)
    public Response attachModule(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") int baseId, @PathVariable("id") int buildingInstId, @PathVariable("module") String moduleId) {
        return controllerManagerWrapper.wrap(() -> buildingManager.attachModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), buildingInstId, moduleId));
    }
}
