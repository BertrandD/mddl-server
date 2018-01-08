package com.middlewar.api.manager;

import com.middlewar.core.exception.BuildingNotFoundException;
import com.middlewar.core.exception.BuildingRequirementMissingException;
import com.middlewar.core.exception.ItemCreationException;
import com.middlewar.core.exception.ItemNotFoundException;
import com.middlewar.core.exception.ItemNotUnlockedException;
import com.middlewar.core.exception.ItemRequirementMissingException;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface FactoryManager {

    /**
     * @param player    the player who is handling this action
     * @param baseId    the base identifier where the module factory is builded
     * @param factoryId the id of the module factory used
     * @param itemId    the template id of the module we want to createFriendRequest  @return the created module
     * @throws ItemRequirementMissingException     if some item requiremments are missing in inventory
     * @throws BuildingRequirementMissingException if some building requiremments are missing
     * @throws ItemNotFoundException               if the given itemId does not match a game item of type module
     * @throws BuildingNotFoundException           if the given module factory doens not exists in the given base
     * @throws ItemNotUnlockedException            if the module is not yet unlocked in the factory
     * @throws ItemCreationException               if something went wrong
     */
    ItemInstance createModule(@NotNull Player player, int baseId, int factoryId, @NotEmpty String itemId);

    /**
     * @param base      the base where the structure factory is builded
     * @param factoryId the id of the module factory used
     * @param itemId    the template id of the structure we want to createFriendRequest  @return the created structure
     * @throws ItemRequirementMissingException     if some item requiremments are missing in inventory
     * @throws BuildingRequirementMissingException if some building requiremments are missing
     * @throws ItemNotFoundException               if the given itemId does not match a game item of type structure
     * @throws BuildingNotFoundException           if the given structure factory doens not exists in the given base
     * @throws ItemNotUnlockedException            if the structure is not yet unlocked in the factory
     * @throws ItemCreationException               if something went wrong
     */
    ItemInstance createStructure(@NotNull Player player, int baseId, int factoryId, @NotEmpty String itemId);

}
