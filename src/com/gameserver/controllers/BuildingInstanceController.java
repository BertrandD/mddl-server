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
        final Player currentPlayer = playerService.findOne(pAccount.getCurrentPlayer());
        return new JsonResponse(currentPlayer.getCurrentBase().getBuildings());
    }

    @JsonView(View.buildingInstance_full.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findOne(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id){
        final Player currentPlayer = playerService.findOne(pAccount.getCurrentPlayer());
        final BuildingInstance building = buildingService.findByBaseAndId(currentPlayer.getCurrentBase(), id);
        if(building == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageData.getInstance().getMessage(pAccount.getLang(), SystemMessageId.BUILDING_NOT_FOUND));
        building.setLang(pAccount.getLang());
        return new JsonResponse(building);
    }

    @JsonView(View.buildingInstance_base.class)
    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "building") String templateId){
        final Player currentPlayer = playerService.findOne(pAccount.getCurrentPlayer());

        final BuildingInstance building = buildingService.create(currentPlayer.getCurrentBase(), templateId);
        if(building == null) return null;

        currentPlayer.getCurrentBase().addBuilding(building);
        baseService.update(currentPlayer.getCurrentBase());

        buildingService.ScheduleUpgrade(building);

        return new JsonResponse(building);
    }

    @JsonView(View.buildingInstance_base.class)
    @RequestMapping(method = RequestMethod.PUT)
    public JsonResponse upgrade(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "building") String id){
        final Player currentPlayer = playerService.findOne(pAccount.getCurrentPlayer());
        final Base base = currentPlayer.getCurrentBase();
        BuildingInstance building = base.getBuildings().stream().filter(k->k.getId().equals(id)).findFirst().orElse(null);
        if(building == null) return null; // TODO: System Message

        buildingService.ScheduleUpgrade(building);

        return new JsonResponse(building);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("id") String id){
        buildingService.delete(id);
    }
}
