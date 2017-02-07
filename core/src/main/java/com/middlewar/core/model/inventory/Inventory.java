package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.model.instances.ItemInstance;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LEBOC Philippe
 */
public abstract class Inventory implements IInventory {

    @Id
    private String id;

    @DBRef
    @JsonManagedReference
    private ConcurrentHashMap<String, ItemInstance> items;

    protected Inventory() {
        setItems(new ConcurrentHashMap<>());
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public ConcurrentHashMap<String, ItemInstance> getItems() {
        return items;
    }

    public void setItems(ConcurrentHashMap<String, ItemInstance> items) {
        this.items = items;
    }
}
