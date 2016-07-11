package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gameserver.model.instances.ItemInstance;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public abstract class AbstractMultiStorageInventory extends AbstractInventory {

    @DBRef
    @JsonManagedReference
    protected List<ItemInstance> items;

    protected AbstractMultiStorageInventory() {
        super();
        setItems(new ArrayList<>());
    }

    public List<ItemInstance> getItems() {
        return items;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }

    public void addItem(ItemInstance item) {
        getItems().add(item);
    }
}
