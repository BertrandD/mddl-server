package com.middlewar.core.model.inventory;

import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.model.instances.ItemInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Inventory implements IInventory {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemInstance> items;

    protected Inventory() {
        setItems(new ArrayList<>());
    }

    public Map<String, ItemInstance> getItemsToMap() {
        final Map<String, ItemInstance> inventoryItems = new HashMap<>();
        items.forEach(itemInstance -> inventoryItems.put(itemInstance.getTemplateId(), itemInstance));
        return inventoryItems;
    }

    @Override
    public abstract long getAvailableCapacity();

    @Override
    public long getId() {
        return id;
    }
}
