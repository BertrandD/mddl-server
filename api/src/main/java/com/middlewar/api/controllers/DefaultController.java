package com.middlewar.api.controllers;

import com.middlewar.api.auth.AccountService;
import com.middlewar.api.services.impl.AstralObjectServiceImpl;
import com.middlewar.api.services.UpdateService;
import com.middlewar.api.util.response.JsonResponse;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.space.AstralObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class DefaultController implements ErrorController{

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private AstralObjectServiceImpl astralObjectServiceImpl;

    @RequestMapping(value = "/")
    public JsonResponse index()
    {
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public JsonResponse resetDatabase(@AuthenticationPrincipal Account pAccount) {
        updateService.resetDatabase();
        pAccount.setCurrentPlayer(null);
        pAccount.getPlayers().clear();
        Account account = accountService.findOne(pAccount.getId());
        account.getPlayers().clear();
        account.setCurrentPlayer(null);
        accountService.update(account);
        AstralObject blackHole = WorldData.getInstance().getWorld();
        astralObjectServiceImpl.saveUniverse(blackHole);
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/resetworld", method = RequestMethod.GET)
    public JsonResponse resetWorld(@AuthenticationPrincipal Account pAccount) {
        AstralObject blackHole = WorldData.getInstance().getWorld();
        astralObjectServiceImpl.saveUniverse(blackHole);
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/reload", method = RequestMethod.GET)
    public JsonResponse reload(){
        BuildingData.getInstance().load();
        ItemData.getInstance().load();
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) {
        final SystemMessageData SystemMessage = SystemMessageData.getInstance();
        final Account account = accountService.findByUsername(username);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(account == null) {
            response.setStatus(401);
            return new JsonResponse(JsonResponseType.ERROR, SystemMessage.getMessage(Lang.EN, SystemMessageId.USERNAME_NOT_FOUND));
        }

        if(!passwordEncoder.matches(password, account.getPassword())) {
            response.setStatus(401);
            return new JsonResponse(JsonResponseType.ERROR, SystemMessage.getMessage(Lang.EN, SystemMessageId.INCORRECT_CREDENTIALS));
        }

        return new JsonResponse(account);
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    public JsonResponse logout(@AuthenticationPrincipal Account account) {
        account.setToken(UUID.randomUUID().toString());
        accountService.update(account);
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonResponse register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        final Account account = accountService.findByUsername(username);
        if(account != null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageData.getInstance().getMessage(Lang.EN, SystemMessageId.ACCOUNT_ALREADY_EXIST));
        return new JsonResponse(accountService.create(username, password));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public JsonResponse aboutMe(@AuthenticationPrincipal Account account) {
        final Account reqAccount = accountService.findOne(account.getId());
        return new JsonResponse(reqAccount);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/lang", method = RequestMethod.POST)
    public JsonResponse changeLanguage(@AuthenticationPrincipal Account account, @RequestParam(value = "lang") String lang) {
        final Lang newLang = Lang.valueOf(lang.toUpperCase()); // TODO: Exception
        final Account currentAccount = accountService.findOne(account.getId());
        account.setLang(newLang);
        currentAccount.setLang(newLang);
        accountService.update(currentAccount);
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = ERROR_PATH)
    public JsonResponse error(HttpServletRequest request) {
        final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return new JsonResponse(JsonResponseType.ERROR, errorAttributes.getErrorAttributes(requestAttributes, false).get("message").toString());
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
