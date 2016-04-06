package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Player;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class PlayerInventory extends Inventory{

    @DBRef
    @JsonView(View.Standard.class)
    private Player owner;

    @DBRef
    @JsonView(View.Standard.class)
    private List<ItemInstance> items;

    public PlayerInventory(){}

    public PlayerInventory(Player player){
        setOwner(player);
    }

    /**
     * All items are allowed to be stored in PlayerInventory
     * @param item
     * @return always true
     */
    @Override
    public boolean isAllowedToStore(ItemInstance item) {
        return true;
    }

    @Override
    public long getMaxCapacity() {
        return 0;
    }

    @Override
    public long getCurrentCapacityCharge() {
        return 0;
    }

    @Override
    public long getFreeCapacity() {
        return 0;
    }

    @Override
    public boolean addItem(ItemInstance item) {
        items.add(item); // TODO add checks
        return true;
    }

    @Override
    public boolean removeItem(String id) {
        final ItemInstance it = items.stream().filter(k->k.getId().equals(id)).findFirst().get();
        if(it != null) items.remove(it); // TODO: add checks
        return true;
    }

    @Override
    public ItemInstance removeAndGet(String id) {
        return null; // TODO
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public List<ItemInstance> getItems() {
        return items;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }
}
