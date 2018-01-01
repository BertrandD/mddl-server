package com.middlewar.api.manager.impl;

import com.middlewar.api.exceptions.BaseCreationException;
import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.PlayerHasNoBaseException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.services.BaseService;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.holders.BuildingInstanceHolder;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.tasks.BuildingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * @author Bertrand
 */
@Service
public class BaseManagerImpl implements BaseManager {

    @Autowired
    private BaseService baseService;

    //@Autowired
    //private BuildingTaskService buildingTaskService;

    @Autowired
    private PlanetManagerImpl planetManager;

    public List<Base> findAllBaseOfPlayer(Player player) {
        final List<Base> bases = player.getBases();
        bases.forEach(Base::initializeStats);
        return bases;
    }

    public Base getBase(long id) {
        final Base base = baseService.findOne((int)id);
        if (base == null) throw new BaseNotFoundException();
        return base;
    }

    public Base getOwnedBase(long id, Player player) {
        final Base base = getBase(id);
        if (base.getOwner().getId() != player.getId()) {
            throw new BaseNotOwnedException();
        }
        return base;
    }

    public Base getCurrentBaseOfPlayer(Player player) {
        if (player.getCurrentBase() == null) {
            throw new PlayerHasNoBaseException();
        }
        return getOwnedBase(player.getCurrentBase().getId(), player);
    }

    public Base getBaseWithBuildingQueue(Player player, long id) {
        final Base base = getOwnedBase(id, player);
        // TODO: add base building queue
        return base;
    }

    public List<BuildingTask> getBaseBuildingQueue(Base base) {
        //return buildingTaskService.findByBaseOrderByEndsAtAsc(base);
        return emptyList();
    }

    public Base create(Player player, String name) {
        final Planet planet = planetManager.pickRandom();

        // TODO: Base creation conditions.

        final Base base = baseService.create(name, player, planet);
        if (base == null) throw new BaseCreationException();
        return base;
    }

    public List<BuildingHolder> getBuildableBuildingsOfBase(Player player, long baseId) {
        final Base base = getOwnedBase(baseId, player);

        final List<BuildingHolder> nextBuildings = new ArrayList<>();

        BuildingData.getInstance().getBuildings().forEach(building ->
        {
            final List<BuildingInstance> myBuildings = base.getBuildings().stream()
                    .filter(k -> k.getBuildingId().equals(building.getId()))
                    .collect(Collectors.toList());

            myBuildings.stream().filter(k -> k.getCurrentLevel() < building.getMaxLevel()).forEach(myBuilding ->
            {
                if (hasRequirements(base, building, myBuilding.getCurrentLevel() + 1))
                    nextBuildings.add(new BuildingInstanceHolder(myBuilding.getId(), building.getId(), myBuilding.getCurrentLevel() + 1));
            });

            if (myBuildings.isEmpty()) {
                if (hasRequirements(base, building, 1))
                    nextBuildings.add(new BuildingHolder(building.getId(), 1));
            }
        });

        return nextBuildings;
    }

    private boolean hasRequirements(Base base, Building building, int nextLevel) {
        boolean hasRequirement = true;
        final Requirement requirement = building.getRequirements().get(nextLevel);

        if (requirement != null) {
            // Check building requirements
            int i = 0;
            while (hasRequirement && i < requirement.getBuildings().size()) {
                final BuildingHolder holder = requirement.getBuildings().get(i);
                final BuildingInstance instOfReqBuilding = base.getBuildings().stream()
                        .filter(k -> k.getBuildingId().equals(holder.getTemplateId()) &&
                                k.getCurrentLevel() >= holder.getLevel()
                        ).findFirst().orElse(null);

                if (instOfReqBuilding == null) hasRequirement = false;
                i++;
            }

            // TODO: Check items requirements
        }
        return hasRequirement;
    }
}
