package com.middlewar.api.manager;

import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.tasks.BuildingTask;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BaseManager {

    /**
     * @param player the player we want the base
     * @return all Base of the currentPlayer of the account
     */
    List<Base> findAllBaseOfPlayer(Player player);

    /**
     * @param id the id of the base to get
     * @return the Base with the given id
     */
    Base getBase(long id);

    /**
     * @param id     base id
     * @param player owner of the base
     * @return the base with the given id and check if the base is owned by the player
     */
    Base getOwnedBase(long id, Player player);

    /**
     * @param player the player we want to get the current base
     * @return the current base of the player
     */
    Base getCurrentBaseOfPlayer(Player player);

    /**
     * @param player the player we want the base
     * @param id     The id of the Base we want the details
     * @return the Base of the current player with the building queue
     */
    Base getBaseWithBuildingQueue(Player player, long id);

    /**
     * @param base the base we want the building queue
     * @return the given base's building queue
     */
    List<BuildingTask> getBaseBuildingQueue(Base base);

    /**
     * Create a base for the given account with the given name
     *
     * @param player the owner of the new Base
     * @param name   the name of the new Base
     * @return the newly created Base
     */
    Base create(Player player, String name);

    /**
     * @param player the player we want the base's buildings
     * @param baseId the id of the base we want the buildings
     * @return the list of all buildings that can be built in the current base of the current player
     */
    List<BuildingHolder> getBuildableBuildingsOfBase(Player player, long baseId);
}
