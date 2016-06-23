package com.gameserver.model.commons;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.ItemHolder;
import com.util.data.json.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Requirement {

    @JsonView(View.Standard.class)
    private int level;

    @JsonView(View.Standard.class)
    private List<ItemHolder> items;

    @JsonView(View.Standard.class)
    private List<BuildingHolder> buildings;

    /**
     * Used to parse ItemData
     * @param items
     * @param buildings
     */
    public Requirement(List<ItemHolder> items, List<BuildingHolder> buildings) {
        setLevel(-1);
        setItems(items);
        setBuildings(buildings);
    }

    /**
     * Used to parse BuildingData
     * @param level
     * @param items
     * @param buildings
     */
    public Requirement(int level, List<ItemHolder> items, List<BuildingHolder> buildings) {
        setLevel(level);
        setItems(items);
        setBuildings(buildings);
    }

    /**
     * Used to parse BuildingData (completion with functions)
     * @param level
     * @param item
     */
    public Requirement(int level, ItemHolder item) {
        setLevel(level);
        setItems(new ArrayList<>());
        setBuildings(new ArrayList<>());
        addItem(item);
    }

    public List<ItemHolder> getItems() {
        return items;
    }

    private void setItems(List<ItemHolder> items) {
        this.items = items;
    }

    public void addItem(ItemHolder item) {
        final ItemHolder alreadyExistingItem = items.stream().filter(k->k.getId().equals(item.getId())).findFirst().orElse(null);
        if(alreadyExistingItem != null)
            alreadyExistingItem.setCount(alreadyExistingItem.getCount()+item.getCount());
        else
            items.add(item);
    }

    public List<BuildingHolder> getBuildings() {
        return buildings;
    }

    private void setBuildings(List<BuildingHolder> buildings) {
        this.buildings = buildings;
    }

    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }
}
