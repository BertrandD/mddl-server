package com.middlewar.controllers;

import com.middlewar.api.exceptions.BuildingAlreadyExistsException;
import com.middlewar.api.exceptions.BuildingCreationException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.BuildingTemplateNotFoundException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.api.manager.BuildingManager;
import com.middlewar.api.manager.PlanetManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.api.util.response.ControllerManagerWrapper;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.MetaHolder;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private AccountManager accountManager;


    @Autowired
    private ControllerManagerWrapper controllerManagerWrapper;

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlanetManager planetManager;

    @Autowired
    private AstralObjectService astralObjectService;

    @Autowired
    private BuildingManager buildingManager;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BuildingService buildingService;

    private Base _base;

    @RequestMapping(value = "/")
    public void test() throws MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException, BuildingTemplateNotFoundException, BuildingAlreadyExistsException, ItemRequirementMissingException, BuildingCreationException, BuildingRequirementMissingException, InterruptedException {
        WorldData.getInstance().reload();
        astralObjectService.saveUniverse();
        Account _account = accountService.create("tt", "");
        Player _player = playerManager.createForAccount(_account, "owner");
        Planet planet = planetManager.pickRandom();
        _base = baseService.create("base1", _player, planet);
        BuildingInstance buildingInstance = buildingManager.create(_base, "silo");
        System.out.println(_base.getBuildings().size());
        TimeUnit.SECONDS.sleep(15);
        System.out.println(_base.getBuildings().size());
    }


//    @RequestMapping(value = "/")
//    public Response index() {
//        return new Response<>(JsonResponseType.SUCCESS);
//    }

    @PreAuthorize("hasRole('ROLE_USER')")
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
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/resetworld", method = RequestMethod.GET)
    public Response resetWorld(@AuthenticationPrincipal Account pAccount) {
        astralObjectService.saveUniverse();
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/reload", method = RequestMethod.GET)
    public Response reload() {
        BuildingData.getInstance().load();
        ItemData.getInstance().load();
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpServletResponse response) {
        return controllerManagerWrapper.wrap(() -> accountManager.login(username, password));
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    public Response logout(@AuthenticationPrincipal Account account) {
        account.setToken(UUID.randomUUID().toString());
        accountService.update(account);
        return new Response<>(JsonResponseType.SUCCESS);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        return controllerManagerWrapper.wrap(() -> accountManager.register(username, password));
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
        accountService.update(currentAccount);
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
