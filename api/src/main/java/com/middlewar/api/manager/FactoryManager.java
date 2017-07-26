package com.middlewar.api.manager;

import com.middlewar.api.exceptions.BuildingNotFoundException;
import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.ItemCreationException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemNotUnlockedException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.services.ValidatorService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.ItemFactory;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Bertrand
 */
@Service
public class FactoryManager {
    private static final String MODULE_FACTORY = "module_factory";
    private static final String STRUCTURE_FACTORY = "structure_factory";

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validator;

    /**
     * @param base the base where the module factory is builded
     * @param factoryId the id of the module factory used
     * @param itemId the template id of the module we want to create  @return the created module
     * @throws ItemRequirementMissingException if some item requiremments are missing in inventory
     * @throws BuildingRequirementMissingException if some building requiremments are missing
     * @throws ItemNotFoundException if the given itemId does not match a game item of type module
     * @throws BuildingNotFoundException if the given module factory doens not exists in the given base
     * @throws ItemNotUnlockedException if the module is not yet unlocked in the factory
     * @throws ItemCreationException if something went wrong
     */
    public ItemInstance createModule(Base base, Long factoryId, String itemId) throws ItemRequirementMissingException, BuildingRequirementMissingException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemCreationException {
        final Item item = ItemData.getInstance().getModule(itemId);
        if(item == null) throw new ItemNotFoundException();

        return createItem(base, factoryId, MODULE_FACTORY, item);
    }

    /**
     * @param base the base where the structure factory is builded
     * @param factoryId the id of the module factory used
     * @param itemId the template id of the structure we want to create  @return the created structure
     * @throws ItemRequirementMissingException if some item requiremments are missing in inventory
     * @throws BuildingRequirementMissingException if some building requiremments are missing
     * @throws ItemNotFoundException if the given itemId does not match a game item of type structure
     * @throws BuildingNotFoundException if the given structure factory doens not exists in the given base
     * @throws ItemNotUnlockedException if the structure is not yet unlocked in the factory
     * @throws ItemCreationException if something went wrong
     */
    public ItemInstance createStructure(Base base, Long factoryId, String itemId) throws ItemRequirementMissingException, BuildingRequirementMissingException, ItemNotFoundException, BuildingNotFoundException, ItemNotUnlockedException, ItemCreationException {
        final Item item = ItemData.getInstance().getStructure(itemId);
        if(item == null) throw new ItemNotFoundException();

        return createItem(base, factoryId, STRUCTURE_FACTORY, item);
    }

    private ItemInstance createItem(Base base, Long factoryId, String factoryType, Item item) throws BuildingNotFoundException, ItemNotUnlockedException, ItemRequirementMissingException, BuildingRequirementMissingException, ItemCreationException {
        base.initializeStats();
        final BuildingInstance factory = base.getBuildings().stream().filter(k->k.getId()==factoryId).findFirst().orElse(null);
        if(factory == null) throw new BuildingNotFoundException();
        if(!factory.getBuildingId().equals(factoryType)) throw new BuildingNotFoundException();

        final ItemFactory factoryTemplate = (ItemFactory)factory.getTemplate();
        if(!factoryTemplate.hasItem(factory.getCurrentLevel(), item.getItemId())) throw new ItemNotUnlockedException();

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        validator.validateItemRequirements(base, item, collector);

        collector.forEach(inventoryService::consumeItem);

        final ItemInstance itemInstance = inventoryService.addItem(base.getBaseInventory(), item.getItemId(), 1);
        if(itemInstance == null) throw new ItemCreationException();

        return itemInstance;
    }
}
