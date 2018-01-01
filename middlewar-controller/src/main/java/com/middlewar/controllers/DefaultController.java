package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.util.response.MetaHolder;
import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import com.middlewar.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private AccountServiceImpl accountService;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AstralObjectService astralObjectService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response index() {
        return new Response();
    }

    @User
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public Response resetDatabase(@AuthenticationPrincipal Account pAccount) {
        //updateService.resetDatabase(); // TODO: CLEANUP ME
        pAccount.setCurrentPlayer(0);
        pAccount.getPlayers().clear();
        Account account = accountService.findOne(pAccount.getId());
        account.getPlayers().clear();
        account.setCurrentPlayer(0);
        return new Response();
    }

    @User
    @RequestMapping(value = "/resetworld", method = RequestMethod.GET)
    public Response resetWorld(@AuthenticationPrincipal Account pAccount) {
        return new Response();
    }

    @RequestMapping(value = "/reload", method = RequestMethod.GET)
    public Response reload() {
        BuildingData.getInstance().load();
        ItemData.getInstance().load();
        return new Response();
    }

    @RequestMapping(value = Route.LOGIN, method = RequestMethod.POST)
    public AccountDto login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) {
        return accountManager.login(username, password).toDTO();
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    public Response logout(@AuthenticationPrincipal Account account) {
        account.setToken(UUID.randomUUID().toString());
        return new Response();
    }

    @RequestMapping(value = Route.REGISTER, method = RequestMethod.POST)
    public AccountDto register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        return accountManager.register(username, password).toDTO();
    }

    @User
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Response aboutMe(@AuthenticationPrincipal Account account) {
        final Account reqAccount = accountService.findOne(account.getId());
        return new Response(reqAccount);
    }

    @User
    @RequestMapping(value = "/lang", method = RequestMethod.POST)
    public Response changeLanguage(@AuthenticationPrincipal Account account, @RequestParam(value = "lang") String lang) {
        final Lang newLang = Lang.valueOf(lang.toUpperCase()); // TODO: Exception
        final Account currentAccount = accountService.findOne(account.getId());
        account.setLang(newLang);
        currentAccount.setLang(newLang);
        return new Response(JsonResponseType.SUCCESS);
    }

    @User
    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public Response getTime() {
        return new Response(new MetaHolder("time", System.currentTimeMillis()));
    }

    @RequestMapping(value = ERROR_PATH)
    public Response error(HttpServletRequest request) {
        final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return new Response(errorAttributes.getErrorAttributes(requestAttributes, false).get("message").toString());
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
