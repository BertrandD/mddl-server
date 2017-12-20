package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bertrand
 */
@User
@RestController
@RequestMapping(produces = "application/json")
public class ReportController {

    @Autowired
    private ReportManager reportManager;

    @Autowired
    private PlayerManager playerManager;

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount) {
        return new Response(reportManager.getAllReportsOfCurrentPlayer(playerManager.getCurrentPlayerForAccount(pAccount)));
    }
}
