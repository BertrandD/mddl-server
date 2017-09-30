package com.middlewar.controllers;

import com.middlewar.api.exceptions.AccountAlreadyExistsException;
import com.middlewar.api.exceptions.ApiException;
import com.middlewar.api.exceptions.IncorrectCredentialsException;
import com.middlewar.api.exceptions.UsernameNotFoundException;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.MetaHolder;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import com.middlewar.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(produces = "application/json")
public class DefaultController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private ControllerManagerWrapper controllerManagerWrapper;

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiException.class)
    public String handleApiException(HttpServletRequest req, ApiException e) {
        return e.getMessage();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response index() {
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public Response resetDatabase(@AuthenticationPrincipal Account pAccount) {
        //updateService.resetDatabase(); // TODO: CLEANUP ME
        pAccount.setCurrentPlayer(0);
        pAccount.getPlayers().clear();
        Account account = accountService.findOne(pAccount.getId());
        account.getPlayers().clear();
        account.setCurrentPlayer(0);
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/resetworld", method = RequestMethod.GET)
    public Response resetWorld(@AuthenticationPrincipal Account pAccount) {
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/reload", method = RequestMethod.GET)
    public Response reload() {
        BuildingData.getInstance().load();
        ItemData.getInstance().load();
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = Route.LOGIN, method = RequestMethod.POST)
    public AccountDTO login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) {
        return accountManager.login(username, password).toDTO();
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    public Response logout(@AuthenticationPrincipal Account account) {
        account.setToken(UUID.randomUUID().toString());
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = Route.REGISTER, method = RequestMethod.POST)
    public AccountDTO register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        return accountManager.register(username, password).toDTO();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Response aboutMe(@AuthenticationPrincipal Account account) {
        final Account reqAccount = accountService.findOne(account.getId());
        return new Response<>(reqAccount);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/lang", method = RequestMethod.POST)
    public Response changeLanguage(@AuthenticationPrincipal Account account, @RequestParam(value = "lang") String lang) {
        final Lang newLang = Lang.valueOf(lang.toUpperCase()); // TODO: Exception
        final Account currentAccount = accountService.findOne(account.getId());
        account.setLang(newLang);
        currentAccount.setLang(newLang);
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public Response getTime() {
        return new Response<>(JsonResponseType.SUCCESS, new MetaHolder("time", System.currentTimeMillis()));
    }

    @RequestMapping(value = ERROR_PATH)
    public Response error(HttpServletRequest request) {
        final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return new Response<>(JsonResponseType.ERROR, errorAttributes.getErrorAttributes(requestAttributes, false).get("message").toString());
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
