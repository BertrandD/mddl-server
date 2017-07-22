package com.middlewar.api.controllers.instances;

import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.api.manager.BuildingTaskManager;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.buildings.ModulableBuilding;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.BuildingTaskService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.ValidatorService;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.SystemMessageId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

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

    @RequestMapping(value = "/me/base/{baseId}/building/{id}", method = RequestMethod.GET)
    public Response getBuilding(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") String baseId, @PathVariable("id") String id) {
        return controllerManagerWrapper.wrap(() -> buildingManager.getBuilding(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), id));
    }

    @RequestMapping(value = "/me/base/{baseId}/building", method = RequestMethod.POST)
    public Response create(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") String baseId, @RequestParam(value = "building") String templateId) {
        return controllerManagerWrapper.wrap(() -> buildingManager.create(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), templateId));
    }

    @RequestMapping(value = "/me/base/{baseId}/building/{id}/upgrade", method = RequestMethod.PUT)
    public Response upgrade(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") String baseId, @PathVariable("id") String id) {
        return controllerManagerWrapper.wrap(() -> buildingManager.upgrade(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), id));
    }

    @RequestMapping(value = "/me/base/{baseId}/building/{id}/attach/module/{module}")
    public Response attachModule(@AuthenticationPrincipal Account pAccount, @PathVariable("baseId") String baseId, @PathVariable("id") String buildingInstId, @PathVariable("module") String moduleId) {
        return controllerManagerWrapper.wrap(() -> buildingManager.attachModule(baseManager.getOwnedBase(baseId, playerManager.getCurrentPlayerForAccount(pAccount)), buildingInstId, moduleId));
    }
}
