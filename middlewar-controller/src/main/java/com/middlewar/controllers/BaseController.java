package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.exceptions.BaseNotFoundException;
import com.middlewar.core.model.Account;
import com.middlewar.request.BaseCreationRequest;
import com.middlewar.request.BaseSpyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.middlewar.core.predicate.BasePredicate.hasId;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author LEBOC Philippe
 */
@User
@Validated
@RestController
@RequestMapping(value = "/base", produces = "application/json")
public class BaseController {

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private ReportManager reportManager;

    @RequestMapping(method = GET)
    public Response findAll(@AuthenticationPrincipal Account account) {
        return new Response(account.getCurrentPlayer().getBases());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Response findOne(@AuthenticationPrincipal Account account, @PathVariable("id") int id) {
        return new Response(account.getCurrentPlayer().getBases().stream().filter(hasId(id)).findFirst().orElseThrow(BaseNotFoundException::new));
    }

    @RequestMapping(method = POST)
    public Response create(@AuthenticationPrincipal Account account, @RequestBody BaseCreationRequest request) {
        return new Response(baseManager.create(account.getCurrentPlayer(), request.getName()));
    }

    @RequestMapping(value = "/spy", method = GET)
    public Response executeSpy(@AuthenticationPrincipal Account account, @RequestBody BaseSpyRequest request) {
        return new Response(reportManager.spy(account.getCurrentPlayer(), request.getSourceId(), request.getTargetId()));
    }
}
