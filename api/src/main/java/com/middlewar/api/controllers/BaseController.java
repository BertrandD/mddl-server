package com.middlewar.api.controllers;

import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.SpyReportManager;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.services.impl.SpyReportServiceImpl;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.holders.BuildingInstanceHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class BaseController {

    private final BaseManager baseManager;

    private final SpyReportManager spyReportManager;

    private final ControllerManagerWrapper controllerManagerWrapper;

    @Autowired
    public BaseController(BaseManager baseManager, SpyReportManager spyReportManager, ControllerManagerWrapper controllerManagerWrapper) {
        this.baseManager = baseManager;
        this.spyReportManager = spyReportManager;
        this.controllerManagerWrapper = controllerManagerWrapper;
    }

    @RequestMapping(value = "/me/base", method = RequestMethod.GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount){
        return controllerManagerWrapper.wrap(() -> baseManager.findAllBaseOfCurrentPlayer(pAccount));
    }

    @RequestMapping(value = "/me/base/{id}", method = RequestMethod.GET)
    public Response findOne(@AuthenticationPrincipal Account account, @PathVariable("id") String id) {
        return controllerManagerWrapper.wrap(() -> baseManager.findOneOfCurrentPlayer(account, id));
    }

    @RequestMapping(value = "/base", method = RequestMethod.POST)
    public Response create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "name") String name) {
        return controllerManagerWrapper.wrap(() -> baseManager.createForAccount(pAccount, name));
    }

    @RequestMapping(value = "/base/buildables", method = RequestMethod.GET)
    public Response calc(@AuthenticationPrincipal Account pAccount) {
        return controllerManagerWrapper.wrap(() -> baseManager.getBuildableBuildingsOfCurrentBase(pAccount));
    }

    @RequestMapping(value = "/base/spy/{id}", method = RequestMethod.GET)
    public Response spy(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        return controllerManagerWrapper.wrap(() -> spyReportManager.spy(pAccount, id));

    }
}
