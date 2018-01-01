package com.middlewar.api.services;

import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface ItemService extends CrudService<ItemInstance, Integer> {
    ItemInstance create(@NotNull Inventory inventory, @NotEmpty String itemId, @Min(1) long count);
}
