package com.middlewar.controllers;

import com.middlewar.api.exceptions.BaseCreationException;
import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.dto.BaseDTO;
import com.middlewar.dto.holder.BuildingHolderDTO;
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

    @RequestMapping(value = Route.BASE_ALL, method = RequestMethod.GET)
    public List<BaseDTO> findAll(@AuthenticationPrincipal Account pAccount) {
        return baseManager.findAllBaseOfPlayer(playerManager.getCurrentPlayerForAccount(pAccount)).stream().map(Base::toDTO).collect(Collectors.toList());
    }

    @RequestMapping(value = Route.BASE_ONE, method = RequestMethod.GET)
    public BaseDTO findOne(@AuthenticationPrincipal Account account, @PathVariable("id") int id) {
        return baseManager.getOwnedBase(id, playerManager.getCurrentPlayerForAccount(account)).toDTO();
    }

    @RequestMapping(value = Route.BASE_CREATE, method = RequestMethod.POST)
    public BaseDTO create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "name") String name) {
        return baseManager.create(playerManager.getCurrentPlayerForAccount(pAccount), name).toDTO();
    }

    @RequestMapping(value = Route.BASE_BUILDABLE, method = RequestMethod.GET)
    public List<BuildingHolderDTO> getBuildables(@AuthenticationPrincipal Account pAccount, @PathVariable("id") int id) {
        return baseManager.getBuildableBuildingsOfBase(playerManager.getCurrentPlayerForAccount(pAccount), id).stream().map(BuildingHolder::toDTO).collect(Collectors.toList());
    }

    @RequestMapping(value = Route.BASE_SPY, method = RequestMethod.GET)
    public Response spy(@AuthenticationPrincipal Account pAccount, @PathVariable("id") int id, @PathVariable("id") int target) {
        return controllerManagerWrapper.wrap(() -> reportManager.spy(playerManager.getCurrentPlayerForAccount(pAccount), id, target));

    }
}
