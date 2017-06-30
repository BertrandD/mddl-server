package com.middlewar.api.controllers;

import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class BaseController {

    private final BaseManager baseManager;

    private final ReportManager reportManager;

    private final ControllerManagerWrapper controllerManagerWrapper;

    @Autowired
    public BaseController(BaseManager baseManager, ReportManager reportManager, ControllerManagerWrapper controllerManagerWrapper) {
        this.baseManager = baseManager;
        this.reportManager = reportManager;
        this.controllerManagerWrapper = controllerManagerWrapper;
    }

    @RequestMapping(value = "/me/base", method = RequestMethod.GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount){
        return controllerManagerWrapper.wrap(() -> baseManager.findAllBaseOfCurrentPlayer(pAccount));
    }

    @RequestMapping(value = "/me/base/{id}", method = RequestMethod.GET)
    public Response findOne(@AuthenticationPrincipal Account account, @PathVariable("id") String id) {
        return controllerManagerWrapper.wrap(() -> baseManager.getBaseWithBuildingQueueOfCurrentPlayer(account, id));
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
        return controllerManagerWrapper.wrap(() -> reportManager.spy(pAccount, id));

    }
}
