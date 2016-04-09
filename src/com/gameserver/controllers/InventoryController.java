package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.services.InventoryService;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/inventory", produces = "application/json")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Inventory> findAll(){
        return inventoryService.findAll();
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Inventory findInventory(@PathVariable("id") String id){
        Inventory inv = inventoryService.findOne(id);
        if(inv == null) return null;
        return inv;
    }
}
