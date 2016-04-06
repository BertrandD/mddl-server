package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.services.InventoryService;
import com.gameserver.services.ItemService;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/item", produces = "application/json")
public class ItemInstanceController {

    @Autowired
    private ItemService itemService;

    private InventoryService inventoryService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<ItemInstance> findAll(){
        return itemService.findAll();
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ItemInstance findOne(@PathVariable(value = "id") String id){
        return itemService.findOne(id);
    }

    @JsonView(View.ItemInstance_Base.class)
    @RequestMapping(method = RequestMethod.POST)
    public ItemInstance create(@RequestParam(value = "itemId") String itemId, @RequestParam(value = "count") long count, @RequestParam(value = "inventory") String inventory) {
        ItemInstance item = itemService.create(itemId, count);
        Inventory inv = inventoryService.findOne(inventory);
        inv.addItem(item);
        inventoryService.update(inv);
        return item;
    }
}
