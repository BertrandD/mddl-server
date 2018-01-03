package com.middlewar.api.manager;

import com.middlewar.api.annotations.model.BaseName;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BaseManager {

    /**
     * Create a base for the given account with the given name
     *
     * @param player the owner of the new Base
     * @param name   the name of the new Base
     * @return the newly created Base
     */
    Base create(@NotNull Player player, @BaseName String name);

    /**
     * @param player the player we want the base's buildings
     * @param baseId the id of the base we want the buildings
     * @return the list of all buildings that can be built in the current base of the current player
     */
    List<BuildingHolder> getBuildableBuildingsOfBase(@NotNull Player player, long baseId);
}
