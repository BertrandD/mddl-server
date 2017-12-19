package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.impl.BaseManagerImpl;
import com.middlewar.api.manager.impl.PlayerManagerImpl;
import com.middlewar.api.manager.impl.ReportManagerImpl;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(produces = "application/json")
public class BaseController {

    @Autowired
    private BaseManagerImpl baseManagerImpl;

    @Autowired
    private PlayerManagerImpl playerManager;

    @Autowired
    private ReportManagerImpl reportManager;

    @RequestMapping(value = "/me/base", method = RequestMethod.GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount) {
        return new Response(baseManagerImpl.findAllBaseOfPlayer(playerManager.getCurrentPlayerForAccount(pAccount)));
    }

    @RequestMapping(value = "/me/base/{id}", method = RequestMethod.GET)
    public Response findOne(@AuthenticationPrincipal Account account, @PathVariable("id") Long id) {
        return new Response(baseManagerImpl.getBaseWithBuildingQueue(playerManager.getCurrentPlayerForAccount(account), id));
    }

    @RequestMapping(value = "/me/base", method = RequestMethod.POST)
    public Response create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "name") String name) {
        return new Response(baseManagerImpl.create(playerManager.getCurrentPlayerForAccount(pAccount), name));
    }

    @RequestMapping(value = "/me/base/{id}/buildables", method = RequestMethod.GET)
    public Response calc(@AuthenticationPrincipal Account pAccount, @PathVariable("id") Long id) {
        return new Response(baseManagerImpl.getBuildableBuildingsOfBase(playerManager.getCurrentPlayerForAccount(pAccount), id));
    }

    @RequestMapping(value = "/me/base/{id}/spy/{target}", method = RequestMethod.GET)
    public Response spy(@AuthenticationPrincipal Account pAccount, @PathVariable("id") Long id, @PathVariable("id") Long target) {
        return new Response(reportManager.spy(playerManager.getCurrentPlayerForAccount(pAccount), id, target));
    }
}
