package com.gameserver.controllers;

import com.auth.Account;
import com.auth.AccountService;
import com.gameserver.services.*;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
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
    AccountService accountService;

    @Autowired
    BaseService baseService;

    @Autowired
    ItemService itemService;

    @Autowired
    BuildingService buildingService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    PlayerService playerService;

    @RequestMapping(value = "/", produces = "application/json")
    public JsonResponse index()
    {
        return new JsonResponse(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET, produces = "application/json")
    public boolean resetDatabase(){
        itemService.deleteAll();
        inventoryService.deleteAll();
        buildingService.deleteAll();
        baseService.deleteAll();
        playerService.deleteAll();
        return true;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public JsonResponse login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password)
    {
        final Account account = accountService.findByUsername(username);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(account == null) return new JsonResponse(JsonResponseType.ERROR, "Username not found");

        if(!passwordEncoder.matches(password, account.getPassword())) return new JsonResponse(JsonResponseType.ERROR, "Invalid credentials");

        final HashMap<String, Object> meta = new HashMap<>();
        meta.put("token", account.getToken());

        return new JsonResponse(JsonResponseType.SUCCESS, meta);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public Account register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        final Account account = accountService.findByUsername(username);
        if(account != null) return null;
        return accountService.create(username, password);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/about/me", method = RequestMethod.GET, produces = "application/json")
    public Account aboutMe(@AuthenticationPrincipal Account account){
        return account;
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
