package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "inventories")
public abstract class Inventory {

    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private HashMap<String, ItemInstance> items;

    public Inventory(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, ItemInstance> getItems() {
        return items;
    }

    public void setItems(HashMap<String, ItemInstance> items) {
        this.items = items;
    }

    public abstract boolean isAllowedToStore(ItemInstance item);

    public abstract long getMaxCapacity();

    public abstract long getFreeCapacity();

    public long getCurrentCapacityCharge(){
        long weight = 0;
        for(ItemInstance item : getItems().values())
        {
            weight += item.getWeight();
        }
        return weight;
    }

    public boolean addItem(ItemInstance item){
        if(isAllowedToStore(item)) {
            if (getItems().isEmpty() && getFreeCapacity() >= item.getWeight()) {
                getItems().put(item.getItemId(), item);
                // TODO: addItem(ItemInstance item, boolean force) => if capacity < item.weight (because item.count very high) add and destoy the rest
                return true;
            }

            // Override count if exist
            final ItemInstance it = getItems().get(item.getItemId());
            if (it != null) {
                if (getFreeCapacity() > getCurrentCapacityCharge() + item.getWeight()) {
                    it.setCount(it.getCount() + item.getCount());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Consume the entire item
     * @param item to consume
     * @return the item with new count value = 0 if consumed, null otherwise
     */
    public ItemInstance consumeItem(ItemInstance item){
        return consumeItem(item.getItemId(), item.getCount());
    }

    public ItemInstance consumeItem(String id, long count){
        if(!getItems().containsKey(id)) return null;

        final ItemInstance item = getItems().get(id);
        if(item.getCount() < count) return null;

        item.setCount(item.getCount() - count);

        return item;
    }
}
