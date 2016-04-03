package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.enums.BuildingType;
import com.gameserver.model.Base;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
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
 * TODO: différencier Building instance & building CONTROLLERS
 */
@RestController
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BaseService baseService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<BuildingInstance> findAll(){
        return buildingService.findAll();
    }

    @JsonView(View.BuildingInstance_Base.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BuildingInstance findOne(@PathVariable("id") String id){
        return buildingService.findOne(id);
    }

    @JsonView(View.BuildingInstance_Base.class)
    @RequestMapping(method = RequestMethod.POST)
    public BuildingInstance create(@RequestParam(value = "base") String baseId, @RequestParam(value = "building") String template){
        Base base = baseService.findOne(baseId);
        if(base == null) return null;
        BuildingInstance building = buildingService.create(base, BuildingType.valueOf(template.toUpperCase())); // TODO: fix IllegalArgument Exception
        if(building == null) return null;
        base.addBuilding(building);
        baseService.update(base);
        return building;
    }
}
