package com.gameserver.controllers;

import com.auth.Account;
import com.auth.AccountService;
import com.gameserver.enums.BuildingType;
import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
import com.gameserver.services.InventoryService;
import com.gameserver.services.ItemService;
import com.gameserver.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    public Account registerPost(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        return accountService.create(username, password);
    }

    @RequestMapping(value = "/header/{id}", produces = "application/json")
    public List<ItemInstance> headerInfo(@PathVariable("id") String id) {
        Base base = baseService.findOne(id);
        if(base == null) return null;

        // Get resources
        final List<ItemInstance> resources = new ArrayList<>();
        base.getBuildings().stream().filter(k->k.getTemplate().getType().equals(BuildingType.STORAGE)).forEach(t->{
            for(final ItemInstance inst : t.getInventory().getItems().values()){
                resources.add(itemService.refreshResource(inst));
            }
        });
        return resources;
    }
}
