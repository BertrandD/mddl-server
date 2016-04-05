package com.gameserver.controllers;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.buildings.Building;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/building_static", produces = "application/json")
public class BuildingController {

    @RequestMapping(method = RequestMethod.GET)
    public List<? extends Building> findAll(){
        return BuildingData.getInstance().getBuildings();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Building findBuilding(@PathVariable("id") String id){
        Building b = BuildingData.getInstance().getBuilding(id);
        if(b == null) return null;
        return b;
    }
}
