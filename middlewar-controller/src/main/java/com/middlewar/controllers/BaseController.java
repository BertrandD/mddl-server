package com.middlewar.controllers;

import com.middlewar.api.exceptions.*;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.dto.holder.BuildingHolderDTO;
import com.middlewar.dto.BaseDTO;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private final PlayerManager playerManager;

    private final ReportManager reportManager;

    private final ControllerManagerWrapper controllerManagerWrapper;

    @Autowired
    public BaseController(BaseManager baseManager, PlayerManager playerManager, ReportManager reportManager, ControllerManagerWrapper controllerManagerWrapper) {
        this.baseManager = baseManager;
        this.playerManager = playerManager;
        this.reportManager = reportManager;
        this.controllerManagerWrapper = controllerManagerWrapper;
    }

    @RequestMapping(value = "/me/base", method = RequestMethod.GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount) {
        return controllerManagerWrapper.wrap(() -> baseManager.findAllBaseOfPlayer(playerManager.getCurrentPlayerForAccount(pAccount)));
    }

    @RequestMapping(value = "/me/base/{id}", method = RequestMethod.GET)
    public BaseDTO findOne(@AuthenticationPrincipal Account account, @PathVariable("id") Long id) throws NoPlayerConnectedException, PlayerNotFoundException, BaseNotFoundException, BaseNotOwnedException {
        return new BaseDTO(baseManager.getOwnedBase(id, playerManager.getCurrentPlayerForAccount(account)));
    }

    @RequestMapping(value = "/me/base", method = RequestMethod.POST)
    public BaseDTO create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "name") String name) throws NoPlayerConnectedException, PlayerNotFoundException, BaseCreationException {
        return new BaseDTO(baseManager.create(playerManager.getCurrentPlayerForAccount(pAccount), name));
    }

    @RequestMapping(value = "/me/base/{id}/buildables", method = RequestMethod.GET)
    public List<BuildingHolderDTO> getBuildables(@AuthenticationPrincipal Account pAccount, @PathVariable("id") Long id) throws NoPlayerConnectedException, PlayerNotFoundException, BaseNotFoundException, BaseNotOwnedException {
        return baseManager.getBuildableBuildingsOfBase(playerManager.getCurrentPlayerForAccount(pAccount), id).stream().map(BuildingHolderDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/me/base/{id}/spy/{target}", method = RequestMethod.GET)
    public Response spy(@AuthenticationPrincipal Account pAccount, @PathVariable("id") Long id, @PathVariable("id") Long target) {
        return controllerManagerWrapper.wrap(() -> reportManager.spy(playerManager.getCurrentPlayerForAccount(pAccount), id, target));

    }
}
