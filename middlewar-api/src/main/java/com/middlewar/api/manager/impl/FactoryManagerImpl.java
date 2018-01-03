package com.middlewar.api.manager.impl;

import com.middlewar.api.manager.FactoryManager;
import com.middlewar.core.exceptions.BaseNotOwnedException;
import com.middlewar.core.exceptions.BuildingNotFoundException;
import com.middlewar.core.exceptions.ItemCreationException;
import com.middlewar.core.exceptions.ItemNotFoundException;
import com.middlewar.core.exceptions.ItemNotUnlockedException;
import com.middlewar.api.services.ValidatorService;
import com.middlewar.api.services.impl.InventoryServiceImpl;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.buildings.ItemFactory;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Item;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

import static com.middlewar.core.predicate.BasePredicate.hasId;

/**
 * @author Bertrand
 */
@Service
@Validated
public class FactoryManagerImpl implements FactoryManager {

    private static final String MODULE_FACTORY = "module_factory";
    private static final String STRUCTURE_FACTORY = "structure_factory";

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Autowired
    private ValidatorService validator;

    @Override
    public ItemInstance createModule(@NotNull Player player, int baseId, int factoryId, @NotEmpty String itemId) {
        final Item item = ItemData.getInstance().getModule(itemId);
        if (item == null) throw new ItemNotFoundException();

        final Base base = player.getBases().stream().filter(hasId(baseId)).findFirst().orElseThrow(BaseNotOwnedException::new);

        return createItem(base, factoryId, MODULE_FACTORY, item);
    }

    @Override
    public ItemInstance createStructure(@NotNull Player player, int baseId, int factoryId, @NotEmpty String itemId) {
        final Item item = ItemData.getInstance().getStructure(itemId);
        if (item == null) throw new ItemNotFoundException();

        final Base base = player.getBases().stream().filter(hasId(baseId)).findFirst().orElseThrow(BaseNotOwnedException::new);

        return createItem(base, factoryId, STRUCTURE_FACTORY, item);
    }

    private ItemInstance createItem(Base base, int factoryId, String factoryType, Item item) {
        base.initializeStats();

        final BuildingInstance factory = base.getBuildings().stream().filter(k -> k.getId() == factoryId).findFirst().orElseThrow(BuildingNotFoundException::new);
        if (!factory.getTemplateId().equals(factoryType)) throw new BuildingNotFoundException();

        final ItemFactory factoryTemplate = (ItemFactory) factory.getTemplate();
        if (!factoryTemplate.hasItem(factory.getCurrentLevel(), item.getItemId())) throw new ItemNotUnlockedException();

        final HashMap<ItemInstance, Long> collector = new HashMap<>();
        validator.validateItemRequirements(base, item, collector);

        collector.forEach(inventoryService::consumeItem);

        final ItemInstance itemInstance = inventoryService.addItem(base.getBaseInventory(), item.getItemId(), 1);
        if (itemInstance == null) throw new ItemCreationException();

        return itemInstance;
    }
}
