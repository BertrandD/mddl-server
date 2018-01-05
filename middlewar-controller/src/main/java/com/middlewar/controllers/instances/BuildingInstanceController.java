package com.middlewar.controllers.instances;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.request.BuildingAttachModuleRequest;
import com.middlewar.request.BuildingCreationRequest;
import com.middlewar.request.BuildingUpgradeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private BuildingManager buildingManager;

    @RequestMapping(method = POST)
    public Response create(@AuthenticationPrincipal Account account, @RequestBody BuildingCreationRequest request) {
        return new Response(buildingManager.create(account.getCurrentPlayer(), request.getBaseId(), request.getTemplateId()));
    }

    @RequestMapping(value = "/upgrade", method = PUT)
    public Response upgrade(@AuthenticationPrincipal Account account, @RequestBody BuildingUpgradeRequest request) {
        return new Response(buildingManager.upgrade(account.getCurrentPlayer(), request.getBaseId(), request.getBuildingId()));
    }

    @RequestMapping(value = "/apply/module", method = PUT)
    public Response attachModule(@AuthenticationPrincipal Account account, @RequestBody BuildingAttachModuleRequest request) {
        return new Response(buildingManager.attachModule(account.getCurrentPlayer(), request.getBaseId(), request.getBuildingId(), request.getModuleTemplateId()));
    }
}
