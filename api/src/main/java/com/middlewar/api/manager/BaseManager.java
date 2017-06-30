package com.middlewar.api.manager;

import com.middlewar.api.exceptions.*;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.BuildingTaskService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.holders.BuildingInstanceHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bertrand
 */
@Service
public class BaseManager {
    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BuildingTaskService buildingTaskService;

    @Autowired
    private PlanetManager planetManager;

    public List<Base> findAllBaseOfCurrentPlayer(Account pAccount) throws NoPlayerConnectedException, PlayerNotFoundException {
        final Player player = playerManager.getPlayerForAccount(pAccount);
        final List<Base> bases = player.getBases();
        bases.forEach(Base::initializeStats);
        return bases;
    }

    /**
     * @param id the id of the base to get
     * @return the Base with the given id
     * @throws BaseNotFoundException if the base does not exists
     */
    public Base getBase(String id) throws BaseNotFoundException {
        final Base base = baseService.findOne(id);
        if(base == null) throw new BaseNotFoundException();
        return base;
    }

    /**
     * @param player owner of the base
     * @param id base id
     * @return the base with the givent id and check if the base is owned by the player
     * @throws BaseNotFoundException if the base boes not exists
     * @throws BaseNotOwnedException if the base is not owned by the given player
     */
    public Base getBase(Player player, String id) throws BaseNotFoundException, BaseNotOwnedException {
        final Base base = getBase(id);
        if (!base.getOwner().getId().equals(player.getId())) {
            throw new BaseNotOwnedException();
        }
        return base;
    }

    /**
     * @param player the player we want to get the base
     * @return the current base of the player
     * @throws BaseNotFoundException if the base does not exists
     * @throws BaseNotOwnedException if the base does not exists
     */
    public Base getBase(Player player) throws BaseNotFoundException, BaseNotOwnedException {
        return getBase(player, player.getCurrentBase().getId());
    }

    public Response findOneOfCurrentPlayer(Account account, String id) throws NoPlayerConnectedException, PlayerNotFoundException, BaseNotFoundException, BaseNotOwnedException {
        Player player = playerManager.getPlayerForAccount(account);
        final Base base = getBase(player, id);
        base.getOwner().setCurrentBase(base);
        playerService.update(base.getOwner());
        final Response response = new Response(base);
        response.addMeta("queue", buildingTaskService.findByBaseOrderByEndsAtAsc(base));
        return response;
    }

    public Base createForAccount(Account account, String name) throws NoPlayerConnectedException, PlayerNotFoundException, BaseCreationException {
        final Player player = playerManager.getPlayerForAccount(account);

        final Planet planet = planetManager.pickRandom();

        // TODO: Base creation conditions.

        final Base base = baseService.create(name, player, planet);
        if(base == null) throw new BaseCreationException();
        return base;
    }

    public  List<BuildingHolder> getBuildableBuildingsOfCurrentBase(Account account) throws NoPlayerConnectedException, PlayerNotFoundException, BaseNotFoundException, BaseNotOwnedException {
        final Player player = playerManager.getPlayerForAccount(account);

        final Base base = getBase(player);

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

        return nextBuildings;
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
