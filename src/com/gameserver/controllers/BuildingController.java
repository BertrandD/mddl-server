package com.gameserver.controllers;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.enums.BuildingType;
import com.gameserver.model.buildings.AbstractBuilding;
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
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingController {

    @RequestMapping(method = RequestMethod.GET)
    public List<? extends AbstractBuilding> findAll(){
        return BuildingData.getInstance().getBuildings();
    }

    @RequestMapping(value = "/building/{type}", method = RequestMethod.GET)
    public Building findBuilding(@PathVariable("type") String type){
        BuildingType bt = BuildingType.valueOf(type);
        return BuildingData.getInstance().getBuilding(bt);
    }
}
