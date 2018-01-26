package com.middlewar.api.manager.impl;

import com.middlewar.api.annotations.model.BaseName;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.exception.BaseCreationException;
import com.middlewar.core.exception.BaseNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.middlewar.core.predicate.BasePredicate.hasId;

/**
 * @author Bertrand
 * @author LEBOC Philippe
 */
@Service
@Validated
public class BaseManagerImpl implements BaseManager {

    @Autowired
    private AstralObjectService astralObjectService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private WorldData world;

    @Override
    public Base create(@NotNull Player player, @BaseName String name) {

        // TODO: Base creation conditions.

        // Find a planet from the World
        final Planet randomPlanet = world.getRandomPlanet();

        // Check if the planet is already in database
        Planet planet = astralObjectService.findPlanetByName(randomPlanet.getName());

        // If it is a fresh planet, store it in database !
        if(planet == null) {
            planet = (Planet) astralObjectService.save(randomPlanet);
        }

        final Base base = baseService.create(name, player, planet);
        if (base == null) throw new BaseCreationException();
        return base;
    }

    @Override
    public List<BuildingHolder> getBuildableBuildingsOfBase(@NotNull Player player, long baseId) {
        final Base base = player.getBases().stream().filter(hasId(baseId)).findFirst().orElseThrow(BaseNotFoundException::new);

        final List<BuildingHolder> nextBuildings = new ArrayList<>();

        BuildingData.getInstance().getBuildings().forEach(building ->
        {
            final List<BuildingInstance> myBuildings = base.getBuildings().stream()
                    .filter(k -> k.getTemplateId().equals(building.getId()))
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
                        .filter(k -> k.getTemplateId().equals(holder.getTemplateId()) &&
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
