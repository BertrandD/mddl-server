package com.middlewar.api.services.impl;

import com.middlewar.api.services.ItemService;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.repository.ItemInstanceRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author LEBOC Philippe
 */
@Service
@Validated
public class ItemServiceImpl extends CrudServiceImpl<ItemInstance, Integer, ItemInstanceRepository> implements ItemService {

    @Override
    public ItemInstance create(@NotNull Inventory inventory, @NotEmpty String itemId, @Min(1) long count) {
        final ItemInstance instance = repository.save(new ItemInstance(inventory, itemId, count));
        inventory.getItems().add(instance);
        return instance;
    }
}
