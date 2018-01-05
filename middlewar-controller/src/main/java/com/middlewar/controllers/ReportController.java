package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author bertrand
 */
@User
@RestController
@RequestMapping(value = "/report", produces = "application/json")
public class ReportController {

    @Autowired
    private ReportManager reportManager;

    @RequestMapping(method = GET)
    public Response findAll(@AuthenticationPrincipal Account account) {
        return new Response(reportManager.getAllReportsOfCurrentPlayer(account.getCurrentPlayer()));
    }
}
