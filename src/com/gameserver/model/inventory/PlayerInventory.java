package com.gameserver.model.inventory;

import com.config.Config;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Player;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class PlayerInventory extends Inventory{

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private Player owner;

    public PlayerInventory(){
        setItems(new HashMap<>());
    }

    public PlayerInventory(Player player){
        setItems(new HashMap<>());
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
        return Config.MAX_PLAYER_INVENTORY_CAPACITY;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
