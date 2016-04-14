package com.gameserver.controllers;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.buildings.Building;
import com.util.data.json.Response.JsonResponse;
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
    public JsonResponse findAll(){
        return new JsonResponse(BuildingData.getInstance().getBuildings());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findBuilding(@PathVariable("id") String id){
        Building b = BuildingData.getInstance().getBuilding(id);
        if(b == null) return null;
        return new JsonResponse(b);
    }
}
