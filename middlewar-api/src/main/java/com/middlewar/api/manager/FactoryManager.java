package com.middlewar.api.manager;

import com.middlewar.api.exceptions.BuildingNotFoundException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.ItemCreationException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemNotUnlockedException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;

/**
 * @author Leboc Philippe.
 */
public interface FactoryManager {

    /**
     * @param base      the base where the module factory is builded
     * @param factoryId the id of the module factory used
     * @param itemId    the template id of the module we want to create  @return the created module
     * @throws ItemRequirementMissingException     if some item requiremments are missing in inventory
     * @throws BuildingRequirementMissingException if some building requiremments are missing
     * @throws ItemNotFoundException               if the given itemId does not match a game item of type module
     * @throws BuildingNotFoundException           if the given module factory doens not exists in the given base
     * @throws ItemNotUnlockedException            if the module is not yet unlocked in the factory
     * @throws ItemCreationException               if something went wrong
     */
    ItemInstance createModule(Base base, Long factoryId, String itemId);

    /**
     * @param base      the base where the structure factory is builded
     * @param factoryId the id of the module factory used
     * @param itemId    the template id of the structure we want to create  @return the created structure
     * @throws ItemRequirementMissingException     if some item requiremments are missing in inventory
     * @throws BuildingRequirementMissingException if some building requiremments are missing
     * @throws ItemNotFoundException               if the given itemId does not match a game item of type structure
     * @throws BuildingNotFoundException           if the given structure factory doens not exists in the given base
     * @throws ItemNotUnlockedException            if the structure is not yet unlocked in the factory
     * @throws ItemCreationException               if something went wrong
     */
    ItemInstance createStructure(Base base, Long factoryId, String itemId);

}
