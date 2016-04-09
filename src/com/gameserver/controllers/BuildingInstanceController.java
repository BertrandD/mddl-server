package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.enums.BuildingType;
import com.gameserver.model.Base;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BuildingInventory;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
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
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ItemService itemService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<BuildingInstance> findAll(){
        return buildingService.findAll();
    }

    @JsonView(View.buildingInstance_full.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BuildingInstance findOne(@PathVariable("id") String id){
        return buildingService.findOne(id);
    }

    @JsonView(View.buildingInstance_base.class)
    @RequestMapping(method = RequestMethod.POST)
    public BuildingInstance create(@RequestParam(value = "base") String baseId, @RequestParam(value = "building") String templateId){
        Base base = baseService.findOne(baseId);
        if(base == null) return null;

        BuildingInstance building = buildingService.create(base, templateId);
        if(building == null) return null;

        base.addBuilding(building);
        baseService.update(base);

        // set the BuildingInventory to the building
        if(building.getTemplate().getType().equals(BuildingType.STORAGE)){
            final BuildingInventory inventory = (BuildingInventory)inventoryService.create(building);
            // generate all ItemInstance with count = 0 if they not exists
            for(String id : building.getStorageBuilding().getFilter().getIds()){
                if(inventory.getItems().get(id) == null){
                    final ItemInstance item = itemService.create(id, 0);
                    if(item != null){
                        inventory.addItem(item);
                        inventoryService.update(inventory);
                    }
                }
            }
        }

        return building;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("id") String id){
        buildingService.delete(id);
    }
}
