package com.middlewar.api.manager;

import com.middlewar.api.exceptions.BaseCreationException;
import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerHasNoBaseException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.util.response.Response;
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
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * @author Bertrand
 */
@Service
public class BaseManager {

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlanetManager planetManager;

    /**
     * @param player the player we want the base
     * @return all Base of the currentPlayer of the account
     */
    public List<Base> findAllBaseOfPlayer(Player player) {
        final List<Base> bases = player.getBases();
        bases.forEach(Base::initializeStats);
        return bases;
    }

    /**
     * @param id the id of the base to get
     * @return the Base with the given id
     * @throws BaseNotFoundException if the base does not exists
     */
    public Base getBase(int id) throws BaseNotFoundException {
        final Base base = baseService.findOne(id);
        if (base == null) throw new BaseNotFoundException();
        return base;
    }

    /**
     * @param id     base id
     * @param player owner of the base
     * @return the base with the given id and check if the base is owned by the player
     * @throws BaseNotFoundException if the base boes not exists
     * @throws BaseNotOwnedException if the base is not owned by the given player
     */
    public Base getOwnedBase(int id, Player player) throws BaseNotFoundException, BaseNotOwnedException {
        final Base base = getBase(id);
        if (base.getOwner().getId() != player.getId()) {
            throw new BaseNotOwnedException();
        }
        return base;
    }

    /**
     * @param player the player we want to get the current base
     * @return the current base of the player
     * @throws BaseNotFoundException if the base does not exists
     * @throws BaseNotOwnedException if the base does not exists
     */
    public Base getCurrentBaseOfPlayer(Player player) throws BaseNotFoundException, BaseNotOwnedException, PlayerHasNoBaseException {
        if (player.getCurrentBase() == null) {
            throw new PlayerHasNoBaseException();
        }
        return getOwnedBase(player.getCurrentBase().getId(), player);
    }

    /**
     * @param player the player we want the base
     * @param id     The id of the Base we want the details
     * @return the Base of the current player with the building queue
     * @throws NoPlayerConnectedException if there is no current player in the account
     * @throws PlayerNotFoundException    if the current player is not found
     * @throws BaseNotFoundException      if the base boes not exists
     * @throws BaseNotOwnedException      if the base is not owned by the given player
     */
    public Response<Base> getBaseWithBuildingQueue(Player player, int id) throws BaseNotFoundException, BaseNotOwnedException {
        final Base base = getOwnedBase(id, player);
        final Response<Base> response = new Response<>(base);
        ArrayList<BuildingTask> queue = new ArrayList<>();
        queue.addAll(getBaseBuildingQueue(base));
        response.addMeta("queue", queue);
        return response;
    }

    /**
     * @param base the base we want the building queue
     * @return the given base's building queue
     */
    public PriorityQueue<BuildingTask> getBaseBuildingQueue(Base base) {
        return base.getBuildingTasks();
    }

    /**
     * Create a base for the given account with the given name
     *
     * @param player the owner of the new Base
     * @param name   the name of the new Base
     * @return the newly created Base
     * @throws NoPlayerConnectedException if there is no current player in the account
     * @throws PlayerNotFoundException    if the current player is not found
     * @throws BaseCreationException      if the Base creation failed
     */
    public Base create(Player player, String name) throws BaseCreationException {
        final Planet planet = planetManager.pickRandom();

        // TODO: Base creation conditions.

        final Base base = baseService.create(name, player, planet);
        if (base == null) throw new BaseCreationException();
        return base;
    }

    /**
     * @param player the player we want the base's buildings
     * @param baseId the id of the base we want the buildings
     * @return the list of all buildings that can be built in the current base of the current player
     * @throws NoPlayerConnectedException if there is no current player in the account
     * @throws PlayerNotFoundException    if the current player is not found
     * @throws BaseNotFoundException      if the base boes not exists
     * @throws BaseNotOwnedException      if the base is not owned by the given player
     */
    public List<BuildingHolder> getBuildableBuildingsOfBase(Player player, int baseId) throws BaseNotFoundException, BaseNotOwnedException {
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
