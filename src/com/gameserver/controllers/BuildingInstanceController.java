package com.gameserver.controllers;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.commons.SystemMessageId;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.services.BaseService;
import com.gameserver.services.BuildingService;
import com.gameserver.services.PlayerService;
import com.util.data.json.Response.JsonResponse;
import com.util.data.json.Response.JsonResponseType;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/building", produces = "application/json")
public class BuildingInstanceController {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BaseService baseService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount){
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        final Map<String, List<BuildingInstance>> allBuildings = new HashMap<>();
        allBuildings.put("buildings", player.getCurrentBase().getBuildings());
        allBuildings.put("buildingQueue", player.getCurrentBase().getBuildingQueue());
        return new JsonResponse(allBuildings);
    }

    @JsonView(View.buildingInstance_full.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findOne(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id){
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        final BuildingInstance building = buildingService.findByBaseAndId(player.getCurrentBase(), id);
        if(building == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageData.getInstance().getMessage(pAccount.getLang(), SystemMessageId.BUILDING_NOT_FOUND));
        building.setLang(pAccount.getLang());
        return new JsonResponse(building);
    }

    @JsonView(View.buildingInstance_base.class)
    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "building") String templateId){
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse("No player selected !"); // TODO: System Message;

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse("Choose a base."); // TODO: System Message;

        final BuildingInstance hasBuilding = buildingService.findByBaseAndBuildingId(player.getCurrentBase(), templateId);
        if(hasBuilding != null) return new JsonResponse("Building already exist !"); // TODO: System Message;

        final BuildingInstance building = buildingService.create(player.getCurrentBase(), templateId);
        if(building == null) return null;

        base.addBuilding(building);
        baseService.update(base);

        buildingService.ScheduleUpgrade(building);

        return new JsonResponse(building);
    }

    @JsonView(View.buildingInstance_base.class)
    @RequestMapping(value = "/upgrade", method = RequestMethod.POST)
    public JsonResponse upgrade(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "building") String id){
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        final Base base = player.getCurrentBase();

        BuildingInstance building = base.getBuildings().stream().filter(k->k.getId().equals(id)).findFirst().orElse(null);
        if(building == null){
            building = base.getBuildingQueue().stream().filter(k->k.getId().equals(id)).findFirst().orElse(null);
            if(building == null) return null; // TODO: System Message
        }

        buildingService.ScheduleUpgrade(building);

        return new JsonResponse(building);
    }
}
