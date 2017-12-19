package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.impl.AccountManagerImpl;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.util.response.MetaHolder;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
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
    private AccountService accountService;

    @Autowired
    private AccountManagerImpl accountManagerImpl;

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
        accountService.update(account);
        astralObjectService.saveUniverse();
        return new Response();
    }

    @User
    @RequestMapping(value = "/resetworld", method = RequestMethod.GET)
    public Response resetWorld() {
        astralObjectService.saveUniverse();
        return new Response();
    }

    @RequestMapping(value = "/reload", method = RequestMethod.GET)
    public Response reload() {
        BuildingData.getInstance().load();
        ItemData.getInstance().load();
        return new Response();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) {
        return new Response(accountManagerImpl.login(username, password));
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    public Response logout(@AuthenticationPrincipal Account account) {
        account.setToken(UUID.randomUUID().toString());
        accountService.update(account);
        return new Response();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        return new Response(accountManagerImpl.register(username, password));
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
        accountService.update(currentAccount);
        return new Response();
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
