package com.gameserver.controllers;

import com.auth.Account;
import com.auth.AccountService;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.services.UpdateService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
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
public class DefaultController implements ErrorController{

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UpdateService updateService;

    @RequestMapping(value = "/", produces = "application/json")
    public JsonResponse index()
    {
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/reset", method = RequestMethod.GET, produces = "application/json")
    public boolean resetDatabase(@AuthenticationPrincipal Account pAccount){
        updateService.resetDatabase();
        pAccount.setCurrentPlayer(null);
        pAccount.getPlayers().clear();
        Account account = accountService.findOne(pAccount.getId());
        account.getPlayers().clear();
        account.setCurrentPlayer(null);
        accountService.update(account);
        return true;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
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

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET, produces = "application/json")
    public JsonResponse logout(@AuthenticationPrincipal Account account) {
        account.setToken(UUID.randomUUID().toString());
        accountService.update(account);
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public JsonResponse register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        final Account account = accountService.findByUsername(username);
        if(account != null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageData.getInstance().getMessage(Lang.EN, SystemMessageId.ACCOUNT_ALREADY_EXIST));
        return new JsonResponse(accountService.create(username, password));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = "application/json")
    public JsonResponse aboutMe(@AuthenticationPrincipal Account account){
        final Account reqAccount = accountService.findOne(account.getId());
        return new JsonResponse(reqAccount);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/lang", method = RequestMethod.POST, produces = "application/json")
    public JsonResponse changeLanguage(@AuthenticationPrincipal Account account, @RequestParam(value = "lang") String lang){
        final Lang newLang = Lang.valueOf(lang.toUpperCase()); // TODO: Exception
        final Account currentAccount = accountService.findOne(account.getId());
        account.setLang(newLang);
        currentAccount.setLang(newLang);
        accountService.update(currentAccount);
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = ERROR_PATH, produces = "application/json")
    public JsonResponse error(HttpServletRequest request) {
        final RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return new JsonResponse(JsonResponseType.ERROR, errorAttributes.getErrorAttributes(requestAttributes, false).get("message").toString());
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
