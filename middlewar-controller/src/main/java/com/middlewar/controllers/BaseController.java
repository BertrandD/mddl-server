package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.model.Account;
import com.middlewar.dto.BaseDto;
import com.middlewar.dto.holder.BuildingHolderDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
@User
@RestController
@RequestMapping(produces = "application/json")
public class BaseController {

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private ReportManager reportManager;

    @RequestMapping(value = Route.BASE_ALL, method = RequestMethod.GET)
    public List<BaseDto> findAll(@AuthenticationPrincipal Account pAccount) {
        return baseManager.findAllBaseOfPlayer(playerManager.getCurrentPlayerForAccount(pAccount)).stream().map(BaseDto::new).collect(Collectors.toList());
    }

    @RequestMapping(value = Route.BASE_ONE, method = RequestMethod.GET)
    public BaseDto findOne(@AuthenticationPrincipal Account account, @PathVariable("id") int id) {
        return baseManager.getOwnedBase(id, playerManager.getCurrentPlayerForAccount(account)).toDTO();
    }

    @RequestMapping(value = Route.BASE_CREATE, method = RequestMethod.POST)
    public BaseDto create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "name") String name) {
        return baseManager.create(playerManager.getCurrentPlayerForAccount(pAccount), name).toDTO();
    }

    @RequestMapping(value = Route.BASE_BUILDABLE, method = RequestMethod.GET)
    public List<BuildingHolderDTO> getBuildables(@AuthenticationPrincipal Account pAccount, @PathVariable("id") int id) {
        return baseManager.getBuildableBuildingsOfBase(playerManager.getCurrentPlayerForAccount(pAccount), id).stream().map(BuildingHolder::toDTO).collect(Collectors.toList());
    }

    @RequestMapping(value = Route.BASE_SPY, method = RequestMethod.GET)
    public Response spy(@AuthenticationPrincipal Account pAccount, @PathVariable("id") int id, @PathVariable("id") int target) {
        return new Response(reportManager.spy(playerManager.getCurrentPlayerForAccount(pAccount), id, target));
    }
}
