package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.inventory.Resource;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface InventoryService extends CrudService<Inventory, Integer> {

    Resource createNewResource(@NotNull final Base base, @NotEmpty final String templateId);
    ItemInstance addItem(@NotNull final Inventory inventory, @NotEmpty final String templateId, @Min(1) final long amount);
    boolean addResource(@NotNull Resource resource, @Min(1) double amount);
    boolean consumeItem(@NotNull ItemInstance item, @Min(1) final long amount);
}
