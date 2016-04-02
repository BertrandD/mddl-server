package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Base;
import com.gameserver.model.items.ItemInstance;
import com.gameserver.services.BaseService;
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

    @Autowired
    private BaseService baseService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<ItemInstance> findAll(){
        return itemService.findAll();
    }

    @JsonView(View.ItemInstance_Base.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ItemInstance findOne(@PathVariable(value = "id") String id){
        return itemService.findOne(id);
    }

    @JsonView(View.ItemInstance_Base.class)
    @RequestMapping(method = RequestMethod.POST)
    public ItemInstance create(@RequestParam(value = "itemId") String itemId, @RequestParam(value = "count") long count, @RequestParam(value = "base") String baseId) {
        Base base = baseService.findOne(baseId);
        if(base == null) return null;
        ItemInstance item = itemService.create(base, itemId, count);
        //base.addItem(item);
        return item;
    }
}
