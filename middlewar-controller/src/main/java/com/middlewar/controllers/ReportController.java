package com.middlewar.controllers;

import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bertrand
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class ReportController {

    private final ReportManager reportManager;

    private final ControllerManagerWrapper controllerManagerWrapper;

    private final PlayerManager playerManager;

    @Autowired
    public ReportController(ReportManager reportManager, ControllerManagerWrapper controllerManagerWrapper, PlayerManager playerManager) {
        this.reportManager = reportManager;
        this.controllerManagerWrapper = controllerManagerWrapper;
        this.playerManager = playerManager;
    }

    @RequestMapping(value = Route.REPORT_ALL, method = RequestMethod.GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount) {
        return controllerManagerWrapper.wrap(() -> reportManager.getAllReportsOfCurrentPlayer(playerManager.getCurrentPlayerForAccount(pAccount)));
    }
}
