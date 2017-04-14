package com.middlewar.api.controllers;

import com.middlewar.api.services.impl.AstralObjectServiceImpl;
import com.middlewar.api.services.impl.SpyReportServiceImpl;
import com.middlewar.api.util.response.JsonResponse;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.holders.BuildingInstanceHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.Star;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.BuildingTaskService;
import com.middlewar.api.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class BaseController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BuildingTaskService buildingTaskService;

    @Autowired
    private SpyReportServiceImpl spyReportServiceImpl;

    @Autowired
    private AstralObjectServiceImpl astralObjectServiceImpl;


    @RequestMapping(value = "/me/base", method = RequestMethod.GET)
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount){
        if(pAccount.getCurrentPlayer() == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);
        final List<Base> bases = player.getBases();
        bases.forEach(Base::initializeStats);
        return new JsonResponse(bases);
    }

    @RequestMapping(value = "/me/base/{id}", method = RequestMethod.GET)
    public JsonResponse findOne(@AuthenticationPrincipal Account account, @PathVariable("id") String id) {
        final Player player = playerService.findOne(account.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);
        final Base base = baseService.findOne(id);
        if(base == null) return new JsonResponse(account.getLang(), SystemMessageId.BASE_NOT_FOUND);
        // Update current player base
        base.getOwner().setCurrentBase(base);
        playerService.update(base.getOwner());
        final JsonResponse response = new JsonResponse(base);
        response.addMeta("queue", buildingTaskService.findByBaseOrderByEndsAtAsc(base));
        return response;
    }

    @RequestMapping(value = "/base", method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account pAccount, @RequestParam(value = "name") String name) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);

        Star star = (Star) astralObjectServiceImpl.findOneByName("S71");

        int nbPlanets = star.getSatellites().size();
        Random random = new Random();
        Planet planet = (Planet)star.getSatellites().get(random.nextInt(nbPlanets));

        // TODO: Base creation conditions.

        final Base base = baseService.create(name, player, planet);
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_CANNOT_CREATE);
        return new JsonResponse(base);
    }

    @RequestMapping(value = "/base/buildables", method = RequestMethod.GET)
    public JsonResponse calc(@AuthenticationPrincipal Account pAccount) {
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = baseService.findOne(player.getCurrentBase().getId());
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        final List<BuildingHolder> nextBuildings = new ArrayList<>();

        BuildingData.getInstance().getBuildings().forEach(building ->
        {
            final List<BuildingInstance> myBuildings = base.getBuildings().stream()
                    .filter(k -> k.getBuildingId().equals(building.getId()))
                    .collect(Collectors.toList());

            myBuildings.stream().filter(k -> k.getCurrentLevel() < building.getMaxLevel()).forEach(myBuilding ->
            {
                if(hasRequirements(base, building, myBuilding.getCurrentLevel()+1))
                    nextBuildings.add(new BuildingInstanceHolder(myBuilding.getId(), building.getId(), myBuilding.getCurrentLevel()+1));
            });

            if(myBuildings.isEmpty())
            {
                if(hasRequirements(base, building, 1))
                    nextBuildings.add(new BuildingHolder(building.getId(), 1));
            }
        });

        return new JsonResponse(nextBuildings);
    }

    /**
     * @param pAccount
     * @return list of all building that can be builded (Checking only buildings requirement)
     */
    @RequestMapping(value = "/base/spy/{id}", method = RequestMethod.GET)
    public JsonResponse spy(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        final Base baseTarget = baseService.findOne(id);
        if(baseTarget == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        final SpyReport report = spyReportServiceImpl.create(player, player.getCurrentBase(), baseTarget);
        if(report == null) return new JsonResponse(JsonResponseType.ERROR, "An error occurred. We can't create the spy report");

        return new JsonResponse(report);
    }

    private boolean hasRequirements(Base base, Building building, int nextLevel)
    {
        boolean hasRequirement = true;
        final Requirement requirement = building.getRequirements().get(nextLevel);

        if(requirement != null)
        {
            // Check building requirements
            int i = 0;
            while(hasRequirement && i < requirement.getBuildings().size())
            {
                final BuildingHolder holder = requirement.getBuildings().get(i);
                final BuildingInstance instOfReqBuilding = base.getBuildings().stream()
                        .filter(k->k.getBuildingId().equals(holder.getTemplateId()) &&
                                k.getCurrentLevel() >= holder.getLevel()
                        ).findFirst().orElse(null);

                if(instOfReqBuilding == null) hasRequirement = false;
                i++;
            }

            // TODO: Check items requirements
        }
        return hasRequirement;
    }
}
