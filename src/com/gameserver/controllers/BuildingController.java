package com.gameserver.controllers;

import com.auth.Account;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.buildings.Building;
import com.util.data.json.Response.JsonResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/building_static", produces = "application/json")
public class BuildingController {

    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount){
        return new JsonResponse(BuildingData.getInstance().getBuildings(pAccount.getLang()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findBuilding(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id){
        Building b = BuildingData.getInstance().getBuilding(id);
        if(b == null) return null; // TODO system message
        b.setLang(pAccount.getLang());
        return new JsonResponse(b);
    }
}
