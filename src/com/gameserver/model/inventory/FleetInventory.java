package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.holders.ItemHolder;
import com.util.data.json.View;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class FleetInventory extends Inventory{

    //@DBRef
    //private Fleet fleet;

    @JsonView(View.Standard.class)
    private List<ItemHolder> items;

    public FleetInventory(){}

    @Override
    public long getMaxWeight() {
        return 0;
    }

    public List<ItemHolder> getItems() {
        return items;
    }

    public void setItems(List<ItemHolder> items) {
        this.items = items;
    }
}
