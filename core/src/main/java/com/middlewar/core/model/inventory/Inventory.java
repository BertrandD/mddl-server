package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.model.instances.ItemInstance;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
public abstract class Inventory implements IInventory {

    @Id
    private String id;

    @DBRef
    @JsonManagedReference
    private List<ItemInstance> items;

    protected Inventory() {
        setItems(new ArrayList<>());
    }

    /**
     * @return an HashMap<TemplateId, ItemInstance>
     */
    public Map<String, ItemInstance> getItemsToMap() {
        final Map<String, ItemInstance> inventoryItems = new HashMap<>();
        items.forEach(itemInstance -> inventoryItems.put(itemInstance.getTemplateId(), itemInstance));
        return inventoryItems;
    }

    @Override
    public abstract long getAvailableCapacity();

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }

    public List<ItemInstance> getItems() {
        return items;
    }
}
