package com.gameserver.controllers;

import com.auth.Account;
import com.auth.AccountService;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
import com.gameserver.services.InventoryService;
import com.gameserver.services.ItemService;
import com.gameserver.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author LEBOC Philippe
 */
@RestController
public class DefaultController {

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
    public String index()
    {
        return "index";
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
}
