package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.inventory.InventoryFilter;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class Storage extends Building {

    private HashMap<Integer, Long> capacityByLevel;

    private InventoryFilter filter;

    public Storage(StatsSet set){
        super(set);
        setCapacityByLevel(new HashMap<>());
    }

    public long getCapacityByLevel(int level){
        return capacityByLevel.get(level);
    }

    public HashMap<Integer, Long> getCapacityByLevel() {
        return capacityByLevel;
    }

    public void setCapacityByLevel(HashMap<Integer, Long> capacity) {
        this.capacityByLevel = capacity;
    }

    public InventoryFilter getFilter() {
        return filter;
    }

    public void setFilter(InventoryFilter filter) {
        this.filter = filter;
    }
}
