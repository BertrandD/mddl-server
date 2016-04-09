package com.gameserver.controllers;

import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
import com.gameserver.services.InventoryService;
import com.gameserver.services.ItemService;
import com.gameserver.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
public class DefaultController {

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
}
