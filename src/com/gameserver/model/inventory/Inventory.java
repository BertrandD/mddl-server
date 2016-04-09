package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "inventories")
public abstract class Inventory {

    @Id
    @JsonView(View.Standard.class)
    private String id;

    public Inventory(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract boolean isAllowedToStore(ItemInstance item);

    public abstract long getMaxCapacity();

    public abstract long getCurrentCapacityCharge();

    public abstract long getFreeCapacity();

    public abstract boolean addItem(ItemInstance item);

    public abstract boolean removeItem(String id);

    public abstract ItemInstance removeAndGet(String id);
}
