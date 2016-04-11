package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.services.ItemService;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/item", produces = "application/json")
public class ItemInstanceController {

    private static final List<String> resources = new ArrayList<>();
    static
    {
        resources.add("100");
        resources.add("101");
        resources.add("102");
    }

    @Autowired
    private ItemService itemService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<ItemInstance> findAll(){
        final List<ItemInstance> items = itemService.findAll();
        return items;
    }

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ItemInstance findOne(@PathVariable(value = "id") String id){
        ItemInstance item = itemService.findOne(id);
        if(item == null) return null;
        /*if(resources.contains(item.getItemId())){
            item = itemService.refresh(item);
        }*/
        return item;
    }

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.POST)
    public ItemInstance create(@RequestParam(value = "itemId") String itemId, @RequestParam(value = "count") long count, @RequestParam(value = "base") String baseId) {
        ItemInstance item = itemService.create(itemId, count);
        return item;
    }
}
