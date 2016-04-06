package com.gameserver.model.inventory;

import com.gameserver.enums.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class InventoryFilter {

    /**
     * List of Items Ids allowed to be stored in inventory
     */
    private List<String> ids;

    /**
     * List of type of items allowed to be stored in inventory
     */
    private List<ItemType> types;

    public InventoryFilter(){
        setIds(new ArrayList<>());
        setTypes(new ArrayList<>());
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<ItemType> getTypes() {
        return types;
    }

    public void setTypes(List<ItemType> types) {
        this.types = types;
    }
}
